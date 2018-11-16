package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.config.mapas.client.service.api.MapaLocalService;
import com.egakat.integration.config.mapas.dto.MapaDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.MapServiceImpl;
import com.egakat.io.gws.client.constants.GwsIntegracionesConstants;
import com.egakat.io.gws.client.constants.GwsMapasConstants;
import com.egakat.io.gws.client.constants.SolicitudDespachoClienteEstadoConstants;

import lombok.val;

@Service
public class SolicitudesDespachoMapServiceImpl extends MapServiceImpl<SolicitudDespachoDto> {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private LookUpService lookUpLocalService;

	@Autowired
	private MapaLocalService mapaLocalService;

	@Override
	protected String getIntegracion() {
		return GwsIntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	protected LookUpService getLookUpService() {
		return lookUpLocalService;
	}

	protected MapaLocalService getMapaService() {
		return mapaLocalService;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.ESTRUCTURA_VALIDA, SolicitudDespachoClienteEstadoConstants.RECIBIDA_OPL);
		return result;
	}

	@Override
	protected MapaDto getMapa(Long id) {
		return getMapaService().findOneById(id);
	}

	@Override
	protected void map(SolicitudDespachoDto model, List<ErrorIntegracionDto> errores) {
		translateCliente(model);
		translateServicio(model);
		translateTercero(model);
		translateCanal(model);
		translateCiudad(model);
		translatePunto(model);

		model.getLineas().parallelStream().forEach(linea -> {
			translateProducto(model, linea);
			translateBodega(model, linea);
			translateEstadoInventario(model, linea);
		});
	}

	private void translateCliente(SolicitudDespachoDto model) {
		String key = defaultKey(model.getClienteCodigoAlterno());

		model.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(key);
		model.setIdCliente(id);
	}

	private void translateServicio(SolicitudDespachoDto model) {
		String key = defaultKey(model.getServicioCodigoAlterno());
		key = getValueFromMapOrDefault(GwsMapasConstants.MAPA_SERVICIO_CODIGO_ALTERNO, key);

		model.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(key);
		model.setIdServicio(id);
	}

	private void translateTercero(SolicitudDespachoDto model) {
		model.setIdTercero(null);

		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(model.getTerceroIdentificacion());

			val id = getLookUpService().findTerceroIdByIdAndNumeroIdentificacion(cliente.longValue(), key);
			model.setIdTercero(id);
		}
	}

	private void translateCanal(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCanalCodigoAlterno());
		key = getValueFromMapOrDefault(GwsMapasConstants.MAPA_CANAL_CODIGO_ALTERNO, key);

		model.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(key);
		model.setIdCanal(id);
	}

	private void translateCiudad(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCiudadCodigoAlterno());
		key = getValueFromMapOrDefault(GwsMapasConstants.MAPA_CIUDAD_CODIGO_ALTERNO, key);

		model.setIdCiudad(null);
		Long id = null;
		if (!StringUtils.isNumeric(key)) {
			id = getLookUpService().findCiudadIdByNombreAlterno(key);
		}

		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo(key);
		}
		model.setIdCiudad(id);
	}

	private void translatePunto(SolicitudDespachoDto model) {
		model.setIdPunto(null);
		val tercero = model.getIdTercero();
		if (tercero != null) {
			String key = defaultKey(model.getPuntoCodigoAlterno());

			val id = getLookUpService().findPuntoIdByTerceroIdAndPuntoCodigo(tercero.longValue(), key);
			model.setIdPunto(id);
		}
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
		key = getValueFromMapOrDefault(GwsMapasConstants.MAPA_BODEGA_CODIGO_ALTERNO, key);

		linea.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		linea.setIdBodega(id);
	}

	private void translateEstadoInventario(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getEstadoInventarioCodigoAlterno());
		key = getValueFromMapOrDefault(GwsMapasConstants.MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, key);

		linea.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(key);
		linea.setIdEstadoInventario(id);
	}

	@Override
	protected void validate(SolicitudDespachoDto model, List<ErrorIntegracionDto> errores) {
		errores.clear();

		List<SolicitudDespachoLineaDto> lineas = model.getLineas();
		if (model.getIdCliente() == null) {
			errores.add(errorAtributoNoHomologado(model, "CLIENTE", model.getClienteCodigoAlterno()));
		}

		if (model.getIdServicio() == null) {
			errores.add(errorAtributoMapeableNoHomologado(model, "SERVICIO", model.getServicioCodigoAlterno(),
					GwsMapasConstants.MAPA_SERVICIO_CODIGO_ALTERNO));
		}

		if (StringUtils.isEmpty(model.getNumeroSolicitud())) {
			errores.add(errorAtributoRequeridoNoSuministrado(model, "NUMERO_SOLICITUD"));
		}
		// TODO VALIDAR numero solicitud no existente

		if (model.getIdCanal() == null) {
			errores.add(errorAtributoMapeableNoHomologado(model, "CANAL", model.getCanalCodigoAlterno(),
					GwsMapasConstants.MAPA_CANAL_CODIGO_ALTERNO));
		}

		if (model.isRequiereTransporte()) {
			if (model.getIdCiudad() == null) {
				errores.add(errorAtributoNoHomologado(model, "CIUDAD", model.getCiudadCodigoAlterno()));
			}

			if (StringUtils.isEmpty(model.getDireccion())) {
				errores.add(errorAtributoRequeridoNoSuministrado(model, "DIRECCION"));
			}
		}

		// TODO VALIDAR ID punto

		lineas.parallelStream().forEach(linea -> {
			// TODO VALIDAR LINEAS Y SUBLINEAS EXTERNAS UNICAS
			if (linea.getIdProducto() == null) {
				errores.add(
						errorAtributoNoHomologado(model, "PRODUCTO", linea.getProductoCodigoAlterno(), asArg(linea)));
			}
			if (linea.getIdBodega() == null) {
				val error = errorAtributoMapeableNoHomologado(model, "BODEGA", linea.getBodegaCodigoAlterno(),
						GwsMapasConstants.MAPA_BODEGA_CODIGO_ALTERNO, asArg(linea));
				error.setArg4(linea.getBodegaCodigoAlterno());
				errores.add(error);
			}
			if (linea.getIdEstadoInventario() == null) {
				errores.add(errorAtributoMapeableNoHomologado(model, "ESTADO_INVENTARIO", linea.getEstadoInventarioCodigoAlterno(),
						GwsMapasConstants.MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, asArg(linea)));
			}
		});

		val bodegas = lineas.stream().filter(a -> a.getIdBodega() != null).map(a -> a.getIdBodega()).distinct()
				.collect(Collectors.toList());

		if (bodegas.size() > 1) {
			errores.add(errorMultiplesBodegasDetectadas(model, "BODEGA"));
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
}
