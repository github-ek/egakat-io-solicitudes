package com.egakat.io.solicitudes.transformation.service.impl;

import static com.egakat.integration.files.enums.EstadoRegistroType.DESCARTADO;
import static com.egakat.integration.files.enums.EstadoRegistroType.ERROR_ENRIQUECIMIENTO;
import static com.egakat.integration.files.enums.EstadoRegistroType.ERROR_HOMOLOGACION;
import static com.egakat.integration.files.enums.EstadoRegistroType.ERROR_VALIDACION;
import static com.egakat.integration.files.enums.EstadoRegistroType.PROCESADO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.PRODUCTO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.BodegaLocalService;
import com.egakat.econnect.maestros.client.service.api.ClienteLocalService;
import com.egakat.integration.files.dto.ArchivoErrorDto;
import com.egakat.integration.files.dto.CampoDto;
import com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom;
import com.egakat.io.solicitudes.repository.manufacturas.ManufacturaBomRepository;
import com.egakat.io.solicitudes.transformation.service.api.ManufacturasBomTransformationService;
import com.egakat.wms.maestros.client.service.api.WhareHouseLocalService;
import com.egakat.wms.maestros.dto.MaterialDto;

import lombok.val;

@Service
public class ManufacturasBomTransformationServiceImpl extends SolicitudesTransformationServiceImpl<ManufacturaBom>
		implements ManufacturasBomTransformationService {
	@Autowired
	private ClienteLocalService clienteService;

	@Autowired
	private BodegaLocalService bodegaService;

	@Autowired
	private WhareHouseLocalService whService;

	@Autowired
	private ManufacturaBomRepository repository;

	@Override
	protected ManufacturaBomRepository getRepository() {
		return repository;
	}

	@Override
	protected void translateField(ManufacturaBom registro, CampoDto campo, String value) {
		switch (campo.getCodigo()) {
		case PRODUCTO_BOM_CODIGO_ALTERNO:
			translateProductoBom(registro, value);
			break;
		case ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			translateEstadoInventarioBom(registro, value);
			break;
		case SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			if (!"".equals(value)) {
				translateSubestadoInventarioBom(registro, value);
			}
			break;
		default:
			super.translateField(registro, campo, value);
		}
	}

	protected void translateProductoBom(ManufacturaBom registro, String value) {
		registro.setIdProductoBom(null);
		val cliente = registro.getIdCliente();
		if (cliente != null) {
			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), value);
			registro.setIdProductoBom(id);
		}
	}

	protected void translateEstadoInventarioBom(ManufacturaBom registro, String value) {
		registro.setIdEstadoInventarioBom(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(value);
		registro.setIdEstadoInventarioBom(id);
	}

	protected void translateSubestadoInventarioBom(ManufacturaBom registro, String value) {
		registro.setIdSubestadoInventarioBom(null);
		val id = getLookUpService().findSubestadoInventarioIdByCodigo(value);
		registro.setIdSubestadoInventarioBom(id);
	}

	@Override
	protected void validateGroups(List<ManufacturaBom> registros, List<ArchivoErrorDto> errores,
			List<CampoDto> campos) {
		super.validateGroups(registros, errores, campos);
		discard(registros);

		val estados = Arrays.asList(PROCESADO, DESCARTADO, ERROR_ENRIQUECIMIENTO, ERROR_HOMOLOGACION);
		val grupos = registros.stream().filter(a -> !estados.contains(a.getEstado()))
				.collect(groupingBy(ManufacturaBom::getIdGrupo));

		if (!grupos.isEmpty()) {
			for (val grupo : grupos.values()) {
				if (validarCantidadesDeProducto(grupo, errores)) {

					val registro = grupo.get(0);
					if (registro.isRequiereBom()) {
						val cliente = clienteService.getByCodigo(registro.getClienteCodigo());
						val bodega = bodegaService.findOneById(registro.getIdBodega());

						val client_id = cliente.getCodigoAlternoWms();
						val wh_id = bodega.getCodigo();
						val prtnum = registro.getProductoCodigoAlterno();
						val invsts = registro.getEstadoInventarioCodigoAlterno();

						boolean existeBom = whService.productoTieneListaDeMateriales(wh_id, client_id, prtnum, invsts);

						if (existeBom) {
							validarMateriales(wh_id, client_id, prtnum, grupo, errores);
						} else {
							errorProductoRequiereUnaListaDeMateriales(errores, registro, wh_id, client_id);
						}
					}
				}
			}
		}
	}

	protected boolean validarCantidadesDeProducto(List<ManufacturaBom> lineas, List<ArchivoErrorDto> errores) {
		boolean result = true;

		val cantidades = lineas.stream().map(ManufacturaBom::getCantidad).distinct().collect(toList());
		if (cantidades.size() > 1) {
			val registro = lineas.get(0);
			val s = cantidades.stream().map(String::valueOf).collect(joining("; "));
			errorCantidadDeProducto(errores, registro, s);
			result = false;
		}

		return result;
	}

	protected void validarMateriales(String wh_id, String client_id, String prtnum, List<ManufacturaBom> grupo,
			List<ArchivoErrorDto> errores) {

		val materialesEsperados = whService.findBom(wh_id, client_id, prtnum);
		val materialesSolicitados = grupo.stream().collect(
				groupingBy(ManufacturaBom::getProductoBomCodigoAlterno, summingInt(ManufacturaBom::getCantidadBom)));

		val registro = grupo.get(0);

		// Lista de materiales como esta configurada para el producto en WMS
		val faltantes = materialesEsperados.stream().map(a -> a.getPrtnum()).collect(toList());

		// Lista de los materiales solicitados en la solicitud
		val sobrantes = new ArrayList<>(materialesSolicitados.keySet());

		// Se le restan a la lista materiales solicitados, la lista de materiales del
		// WMS, si quedan elementos en la lista significa que se pidieron materiales de
		// mas.
		sobrantes.removeAll(faltantes);

		// Se le resta a la lista materiales del WMS, la lista de materiales
		// solicitados, si quedan elementos en la lista significa que no se pidieron
		// todos los materiales requeridos por WMS.
		faltantes.removeAll(materialesSolicitados.keySet());

		if (!sobrantes.isEmpty()) {
			errorMaterialesSobrantes(errores, registro, wh_id, client_id, String.join(" ; ", sobrantes));
		}

		if (!faltantes.isEmpty()) {
			errorMaterialesFaltantes(errores, registro, wh_id, client_id, String.join(" ; ", sobrantes));
		}

		if (sobrantes.isEmpty() && faltantes.isEmpty()) {
			for (val material : materialesEsperados) {
				val cantidadProducto = new BigDecimal(registro.getCantidad());
				val cantidadMaterialEsperada = material.getCnsqty().multiply(cantidadProducto);
				val cantidadMaterialSolicitada = new BigDecimal(materialesSolicitados.get(material.getPrtnum()));

				if (cantidadMaterialEsperada.compareTo(cantidadMaterialSolicitada) != 0) {
					errorCantidadDeMateriales(errores, registro, wh_id, client_id, material, cantidadMaterialEsperada,
							cantidadMaterialSolicitada);
				}
			}
		}
	}

	protected void errorCantidadDeProducto(List<ArchivoErrorDto> errores, ManufacturaBom registro, String cantidades) {
		val format = "El producto con código %s y estado %s presenta cantidades diferentes en la misma solicitud. Totalice por solicitud las cantidades de producto por estado.";

		val mensaje = String.format(format, registro.getProductoCodigoAlterno(), registro.getIdEstadoInventario());
		val sb = getDatosError(registro);
		sb.append(", ");
		sb.append("cantidades=[").append(cantidades).append("] ");

		val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(), sb.toString());
		errores.add(error);
		registro.setEstado(ERROR_VALIDACION);
	}

	protected void errorProductoRequiereUnaListaDeMateriales(List<ArchivoErrorDto> errores,
			ManufacturaBom registro, String wh_id, String client_id) {
		val mensaje = "El producto no tiene asociada una lista de materiales";
		val sb = getDatosError(registro);
		sb.append(", ");
		sb.append("wh_id=").append(wh_id).append(", ");
		sb.append("client_id=").append(client_id).append(", ");
		sb.append("prtnum=").append(registro.getProductoCodigoAlterno());

		val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(), sb.toString());
		errores.add(error);
		registro.setEstado(ERROR_VALIDACION);
	}

	protected void errorMaterialesSobrantes(List<ArchivoErrorDto> errores, ManufacturaBom registro, String wh_id,
			String client_id, String sobrantes) {
		val mensaje = "Se incluyeron productos de mas en la lista de materiales solicitada.";
		val sb = getDatosError(registro);
		sb.append(", ");
		sb.append("wh_id=").append(wh_id).append(", ");
		sb.append("client_id=").append(client_id).append(", ");
		sb.append("prtnum=").append(registro.getProductoCodigoAlterno());

		val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(), sb.toString());
		errores.add(error);
		registro.setEstado(ERROR_VALIDACION);
	}

	protected void errorMaterialesFaltantes(List<ArchivoErrorDto> errores, ManufacturaBom registro, String wh_id,
			String client_id, String faltantes) {
		val mensaje = "No se incluyeron todos los productos en la lista de materiales solicitada.";
		val sb = getDatosError(registro);
		sb.append(", ");
		sb.append("wh_id=").append(wh_id).append(", ");
		sb.append("client_id=").append(client_id).append(", ");
		sb.append("prtnum=").append(registro.getProductoCodigoAlterno());

		val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(), sb.toString());
		errores.add(error);
		registro.setEstado(ERROR_VALIDACION);
	}

	protected void errorCantidadDeMateriales(List<ArchivoErrorDto> errores, ManufacturaBom registro, String wh_id,
			String client_id, MaterialDto material, BigDecimal cantidadMaterialEsperada,
			BigDecimal cantidadMaterialSolicitada) {
		val mensaje = "La cantidad total de material solicitado no corresponde a la cantidad de material requerido para producir la cantidad de producto";

		val sb = getDatosError(registro);
		sb.append(", ");
		sb.append("cantidad producto=").append(registro.getCantidad()).append(", ");
		sb.append("wh_id=").append(wh_id).append(", ");
		sb.append("client_id=").append(client_id).append(", ");
		sb.append("prtnum=").append(registro.getProductoCodigoAlterno()).append(", ");
		sb.append("material=").append(material.getPrtnum()).append(", ");
		sb.append("cnsqty=").append(material.getCnsqty().toPlainString()).append(", ");
		sb.append("total material esperado=").append(cantidadMaterialEsperada.toPlainString()).append(", ");
		sb.append("total material solicitado=").append(cantidadMaterialSolicitada.toPlainString());

		val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(), sb.toString());
		errores.add(error);
		registro.setEstado(ERROR_VALIDACION);
	}

	protected StringBuilder getDatosError(ManufacturaBom registro) {
		val sb = new StringBuilder();
		sb.append("bodega=").append(registro.getBodegaCodigoAlterno()).append(", ");
		sb.append("numero_solicitud=").append(registro.getNumeroSolicitud()).append(", ");
		sb.append("cliente=").append(registro.getClienteCodigo()).append(", ");
		sb.append("producto=").append(registro.getProductoCodigoAlterno()).append(", ");
		sb.append("estado=").append(registro.getIdEstadoInventario());
		return sb;
	}
}