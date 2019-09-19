package com.egakat.io.clientes.diageo.service.impl;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.mapas.dto.MapaDto;
import com.egakat.integration.mapas.service.api.MapaCrudService;
import com.egakat.integration.service.impl.MapServiceImpl;
import com.egakat.io.clientes.diageo.constants.DiageoSolicitudesDespachoConstants;
import com.egakat.io.clientes.diageo.constants.MapasConstants;
import com.egakat.io.clientes.diageo.service.api.DiageoSolicitudDespachoMapService;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class DiageoSolicitudDespachoMapServiceImpl extends MapServiceImpl<SolicitudDespachoDto>
		implements DiageoSolicitudDespachoMapService {

	private static LocalDate MIN_DATE = LocalDate.now().minusDays(90);
	private static LocalDate MAX_DATE = LocalDate.now().plusDays(90);

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private LookUpService lookUpLocalService;

	@Autowired
	private MapaCrudService mapaService;

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected String getIntegracion() {
		return DiageoSolicitudesDespachoConstants.INTEGRACION_CODIGO;
	}

	protected LookUpService getLookUpService() {
		return lookUpLocalService;
	}

	protected MapaCrudService getMapaService() {
		return mapaService;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;

		val result = getActualizacionesService()
				.findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(getIntegracion(), estado, "");
		return result;
	}

	@Override
	protected void map(SolicitudDespachoDto input, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		translateCliente(input);
		translateServicio(input);
		translateCanal(input);
		translateCiudad(input);

		input.getLineas().parallelStream().forEach(linea -> {
			translateProducto(input, linea);
			translateBodega(input, linea);
			translateEstadoInventario(input, linea);
		});
	}

	@Override
	protected MapaDto getMapa(Long id) {
		return getMapaService().findOneById(id);
	}

	private void translateCliente(SolicitudDespachoDto model) {
		String key = defaultKey(model.getClienteCodigoAlterno());

		model.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(key);
		model.setIdCliente(id);
	}

	private void translateServicio(SolicitudDespachoDto model) {
		String key = defaultKey(model.getServicioCodigoAlterno());

		model.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(key);
		model.setIdServicio(id);
	}

	private void translateCanal(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCanalCodigoAlterno());

		model.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(key);
		model.setIdCanal(id);
	}

	private void translateCiudad(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCiudadCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_CIUDADES_CODIGO_ALTERNO, key);

		model.setIdCiudad(null);
		Long id = null;
		if (!StringUtils.isNumeric(key)) {
			id = getLookUpService().findCiudadIdByNombreAlterno(key);
		}

		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo(key);
		}
		
		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo("00000");
		}
		
		model.setIdCiudad(id);
	}

	private void translateProducto(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		linea.setIdProducto(null);
		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(linea.getProductoCodigoAlterno());

			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), key);
			linea.setIdProducto(id);
		}
	}

	private void translateBodega(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getBodegaCodigoAlterno());

		linea.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		linea.setIdBodega(id);
	}

	private void translateEstadoInventario(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getEstadoInventarioCodigoAlterno());

		linea.setIdEstadoInventario(key);
	}

	@Override
	protected void validate(SolicitudDespachoDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		errores.clear();

		List<SolicitudDespachoLineaDto> lineas = input.getLineas();
		if (input.getIdCliente() == null) {
			errores.add(errorAtributoNoHomologado(input, "CLIENTE", input.getClienteCodigoAlterno()));
		}

		if (input.getIdServicio() == null) {
			errores.add(errorAtributoNoHomologado(input, "SERVICIO", input.getServicioCodigoAlterno()));
		}

		if (isEmpty(input.getNumeroSolicitud())) {
			errores.add(errorAtributoRequeridoNoSuministrado(input, "NUMERO_SOLICITUD"));
		}

		if (input.getIdCanal() == null) {
			errores.add(errorAtributoNoHomologado(input, "CANAL", input.getCanalCodigoAlterno()));
		}

		if (input.isRequiereTransporte()) {
			if (input.getIdCiudad() == null) {
				errores.add(errorAtributoNoHomologado(input, "CIUDAD", input.getCiudadCodigoAlterno()));
			}

			if (isEmpty(input.getDireccion())) {
				errores.add(errorAtributoRequeridoNoSuministrado(input, "DIRECCION"));
			}
		}

		if (input.getFemi().isBefore(MIN_DATE)) {
			errores.add(errorFechaPorDebajoDelMinimo(input, "FEMI", input.getFemi(), MIN_DATE));
		} else {
			if (input.getFemi().isAfter(MAX_DATE)) {
				errores.add(errorFechaPorEncimaDelMaximo(input, "FEMI", input.getFemi(), MAX_DATE));
			}
		}

		if (input.getFema().isBefore(MIN_DATE)) {
			errores.add(errorFechaPorDebajoDelMinimo(input, "FEMA", input.getFema(), MIN_DATE));
		} else {
			if (input.getFema().isAfter(MAX_DATE)) {
				errores.add(errorFechaPorEncimaDelMaximo(input, "FEMA", input.getFema(), MAX_DATE));
			}
		}

		lineas.parallelStream().forEach(linea -> {
			if (linea.getIdProducto() == null) {
				errores.add(
						errorAtributoNoHomologado(input, "PRODUCTO", linea.getProductoCodigoAlterno(), asArg(linea)));
			}

			if (linea.getCantidad() <= 0) {
				errores.add(errorNumeroPorDebajoDelMinimo(input, "CANTIDAD", linea.getCantidad(), 0, asArg(linea)));
			}

			if (linea.getIdBodega() == null) {
				errores.add(
						errorAtributoNoHomologado(input, "BODEGA", linea.getBodegaCodigoAlterno(), asArg(linea)));
			}

			if (linea.getIdEstadoInventario() == null) {
				errores.add(errorAtributoNoHomologado(input, "ESTADO_INVENTARIO",
						linea.getEstadoInventarioCodigoAlterno(), asArg(linea)));
			}
		});

		val bodegas = lineas.stream().filter(a -> a.getIdBodega() != null).map(a -> a.getIdBodega()).distinct()
				.collect(Collectors.toList());

		if (bodegas.size() > 1) {
			errores.add(errorMultiplesBodegasDetectadas(input, "BODEGA"));
		}
	}

	private String[] asArg(SolicitudDespachoLineaDto linea, String... args) {
		val result = new String[6 + args.length];
		int i = 0;
		result[i++] = String.valueOf(linea.getNumeroLinea());
		result[i++] = linea.getNumeroLineaExterno();
		result[i++] = linea.getNumeroSubLineaExterno();
		result[i++] = linea.getProductoCodigoAlterno();
		result[i++] = linea.getProductoNombre();
		result[i++] = String.valueOf(linea.getCantidad());

		for (val a : args) {
			result[i++] = a;
		}

		return result;
	}

	private ErrorIntegracionDto errorMultiplesBodegasDetectadas(SolicitudDespachoDto entry, String codigo,
			String... arg) {
		val mensaje = "Se detecto que esta solicitud esta asociada a mas de una bodega.";
		val result = getErroresService().error(entry, codigo, mensaje, arg);
		return result;
	}

	@Override
	protected void onSuccess(SolicitudDespachoDto input, ActualizacionDto actualizacion) {
		EstadoIntegracionType estado = EstadoIntegracionType.VALIDADO;
		String subestado = "";

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(SolicitudDespachoDto input, ActualizacionDto actualizacion) {
		getCrudService().update(input, actualizacion, actualizacion.getEstadoIntegracion());
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estado = EstadoIntegracionType.ERROR_VALIDACION;

		actualizacion.setEstadoIntegracion(estado);
	}
}
