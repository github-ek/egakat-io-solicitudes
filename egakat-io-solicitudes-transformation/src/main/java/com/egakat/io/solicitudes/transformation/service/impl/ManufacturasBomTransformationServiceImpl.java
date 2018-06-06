package com.egakat.io.solicitudes.transformation.service.impl;

import static com.egakat.integration.files.enums.EstadoRegistroType.ERROR_VALIDACION;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.PRODUCTO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
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
	protected boolean validateRow(ManufacturaBom registro, List<ArchivoErrorDto> errores, List<CampoDto> campos) {
		boolean result = super.validateRow(registro, errores, campos);

		if (result) {
			if (registro.isRequiereBom()) {
				val cliente = clienteService.getByCodigo(registro.getClienteCodigo());
				val bodega = bodegaService.findOneById(registro.getIdBodega());

				val client_id = cliente.getCodigoAlternoWms();
				val wh_id = bodega.getCodigo();
				val prtnum = registro.getProductoCodigoAlterno();
				val invsts = registro.getEstadoInventarioCodigoAlterno();

				result = whService.productoTieneListaDeMateriales(wh_id, client_id, prtnum, invsts);
				if (!result) {
					val mensaje = "El producto no tiene asociada una lista de materiales";
					val datos = String.format("client_id=%s, wh_id=%s, prtnum=%s", client_id, wh_id, prtnum);
					val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(),
							datos);

					errores.add(error);
				} else {
					val materiales = whService.findBom(wh_id, client_id, prtnum);
					
					//chequee que no hayan fraciones de producto en la lista de materiales
					
				}
			}
		}

		return result;
	}

	@Override
	protected void validateGroups(List<ManufacturaBom> registros, ArrayList<ArchivoErrorDto> errores,
			List<CampoDto> campos) {
		// super.validateGroups(registros, errores, campos);
		// discard(registros);
		//
		// val estados = Arrays.asList(PROCESADO, DESCARTADO, ERROR_ENRIQUECIMIENTO,
		// ERROR_HOMOLOGACION);
		// val groups = registros.stream().filter(a -> !estados.contains(a.getEstado()))
		// .collect(groupingBy(ManufacturaBom::getIdGrupo));
		//
		// for (val key : groups.keySet()) {
		// val lineas = groups.get(key);
		//
		// if (checkCantidadesLinea(lineas, errores)) {
		// checkCantidadesBom(lineas, errores);
		// }
		// }
		//
		// System.out.println(groups);
	}

	protected boolean checkCantidadesLinea(List<ManufacturaBom> lineas, ArrayList<ArchivoErrorDto> errores) {
		boolean result = true;

		val cantidades = lineas.stream().map(ManufacturaBom::getCantidad).distinct().collect(toList());
		if (cantidades.size() > 1) {
			val mensaje = getMensajeErrorMultiplesCantidades(lineas.get(0));
			val datos = getDatosErrorMultiplesCantidades(lineas.get(0), cantidades);

			for (val linea : lineas) {
				val error = ArchivoErrorDto.error(linea.getIdArchivo(), mensaje, linea.getNumeroLinea(), datos);
				linea.setEstado(ERROR_VALIDACION);
				errores.add(error);
			}
			result = false;
		}

		return result;
	}

	protected String getMensajeErrorMultiplesCantidades(ManufacturaBom linea) {
		String format;
		String mensaje;
		format = "El producto con código %s y estado %s presenta cantidades diferentes en la misma solicitud. Totalice las cantidades de producto por estado.";
		mensaje = String.format(format, linea.getProductoCodigoAlterno(), linea.getIdEstadoInventario());
		return mensaje;
	}

	protected String getDatosErrorMultiplesCantidades(ManufacturaBom linea, List<Integer> cantidades) {
		val sb = new StringBuilder();
		sb.append("cliente=").append(linea.getClienteCodigo());
		sb.append(", solicitud=").append(linea.getNumeroSolicitud());
		sb.append(", producto=").append(linea.getProductoCodigoAlterno());
		sb.append(", estado=").append(linea.getIdEstadoInventario());
		sb.append(", cantidades=").append(cantidades.stream().map(String::valueOf).collect(joining(", ")));
		return sb.toString();
	}

	protected void checkCantidadesBom(List<ManufacturaBom> lineas, ArrayList<ArchivoErrorDto> errores) {
		val linea = lineas.get(0);
		val subgrupos = lineas.stream().collect(groupingBy(ManufacturaBom::getIdProductoBom));

		for (val productoId : subgrupos.keySet()) {
			val sublineas = subgrupos.get(productoId);
			int linqty = sublineas.stream().mapToInt(ManufacturaBom::getCantidadBom).sum();
			int mod = linqty % linea.getCantidad();

			if (mod > 0) {
				val mensaje = getMensajeErrorCantidadBom(lineas.get(0), linqty);
				val datos = getDatosErrorCantidadBom(lineas.get(0), linqty);

				for (val sublinea : sublineas) {
					val error = ArchivoErrorDto.error(sublinea.getIdArchivo(), mensaje, sublinea.getNumeroLinea(),
							datos);
					sublinea.setEstado(ERROR_VALIDACION);
					errores.add(error);
				}
			}
		}
	}

	private String getMensajeErrorCantidadBom(ManufacturaBom linea, int linqty) {
		String format;
		String mensaje;
		format = "Se está solicitando un total de %d unidades del material con código %s para armar %d unidades del producto %s en estado %s. El número total de unidades del material solicitado debe ser divisible entre el número de unidades del producto que se va armar.";
		mensaje = String.format(format, linqty, linea.getProductoBomCodigoAlterno(), linea.getCantidad(),
				linea.getProductoCodigoAlterno(), linea.getIdEstadoInventario());
		return mensaje;
	}

	private String getDatosErrorCantidadBom(ManufacturaBom linea, int linqty) {
		val sb = new StringBuilder();
		sb.append("cliente=").append(linea.getClienteCodigo());
		sb.append(", solicitud=").append(linea.getNumeroSolicitud());
		sb.append(", producto=").append(linea.getProductoCodigoAlterno());
		sb.append(", estado=").append(linea.getIdEstadoInventario());
		sb.append(", cantidad=").append(linea.getCantidad());
		sb.append(", linqty=").append(linqty);
		return sb.toString();
	}
}