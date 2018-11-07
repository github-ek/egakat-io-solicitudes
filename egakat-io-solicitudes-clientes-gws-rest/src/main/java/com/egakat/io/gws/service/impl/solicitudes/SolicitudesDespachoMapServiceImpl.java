package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.config.mapas.client.service.api.MapaLocalService;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.core.service.impl.MapServiceImpl;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.configuration.constants.SolicitudEstadoConstants;

import lombok.val;

@Service
public class SolicitudesDespachoMapServiceImpl extends MapServiceImpl<SolicitudDespachoDto> {

	protected static long MAPA_SERVICIO_CODIGO_ALTERNO = 200;
	protected static long MAPA_CANAL_CODIGO_ALTERNO = 201;
	protected static long MAPA_CIUDAD_CODIGO_ALTERNO = 209;
	protected static long MAPA_BODEGA_CODIGO_ALTERNO = 202;
	protected static long MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO = 203;

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private LookUpService lookUpLocalService;

	@Autowired
	private MapaLocalService mapaLocalService;

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected ExtendedIntegracionEntityCrudService<SolicitudDespachoDto> getCrudService() {
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
				getIntegracion(), EstadoIntegracionType.ESTRUCTURA_VALIDA, SolicitudEstadoConstants.RECIBIDA_OPL);
		return result;
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

	protected void translateCliente(SolicitudDespachoDto model) {
		String key = defaultKey(model.getClienteCodigoAlterno());

		model.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(key);
		model.setIdCliente(id);
	}

	protected void translateServicio(SolicitudDespachoDto model) {
		String key = defaultKey(model.getServicioCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_SERVICIO_CODIGO_ALTERNO, key);

		model.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(key);
		model.setIdServicio(id);
	}

	protected void translateTercero(SolicitudDespachoDto model) {
		model.setIdTercero(null);

		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(model.getTerceroIdentificacion());

			val id = getLookUpService().findTerceroIdByIdAndNumeroIdentificacion(cliente.longValue(), key);
			model.setIdTercero(id);
		}
	}

	protected void translateCanal(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCanalCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_CANAL_CODIGO_ALTERNO, key);

		model.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(key);
		model.setIdCanal(id);
	}

	protected void translateCiudad(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCiudadCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_CIUDAD_CODIGO_ALTERNO, key);

		model.setIdCiudad(null);
		Long id = null;
		if(!StringUtils.isNumeric(key)) {
			id = getLookUpService().findCiudadIdByNombreAlterno(key);
		}

		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo(key);
		}
		model.setIdCiudad(id);
	}

	protected void translatePunto(SolicitudDespachoDto model) {
		model.setIdPunto(null);
		val tercero = model.getIdTercero();
		if (tercero != null) {
			String key = defaultKey(model.getPuntoCodigoAlterno());

			val id = getLookUpService().findPuntoIdByTerceroIdAndPuntoCodigo(tercero.longValue(), key);
			model.setIdPunto(id);
		}
	}

	protected void translateProducto(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		linea.setIdProducto(null);
		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(linea.getProductoCodigoAlterno());

			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), key);
			linea.setIdProducto(id);
		}
	}

	protected void translateBodega(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getBodegaCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_BODEGA_CODIGO_ALTERNO, key);

		linea.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		linea.setIdBodega(id);
	}

	protected void translateEstadoInventario(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getEstadoInventarioCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, key);

		linea.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(key);
		linea.setIdEstadoInventario(id);
	}

	protected String defaultKey(String _default) {
		return StringUtils.defaultString(_default).toUpperCase();
	}

	protected String getValueFromMapOrDefault(Long id, String _default) {
		String result = _default;
		if (id != null) {
			val mapa = getMapaService().findOneById(id);
			val value = mapa.getValores().get(_default);
			if (value != null) {
				result = value;
			}
		}
		return result;
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
					MAPA_SERVICIO_CODIGO_ALTERNO));
		}

		if (StringUtils.isEmpty(model.getNumeroSolicitud())) {
			errores.add(errorAtributoRequeridoNoSuministrado(model, "NUMERO_SOLICITUD"));
		}
		// TODO VALIDAR numero solicitud no existente

		if (model.getIdCanal() == null) {
			errores.add(errorAtributoMapeableNoHomologado(model, "CANAL", model.getCanalCodigoAlterno(),
					MAPA_CANAL_CODIGO_ALTERNO));
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
						MAPA_BODEGA_CODIGO_ALTERNO, asArg(linea));
				error.setArg4(linea.getBodegaCodigoAlterno());
				errores.add(error);
			}
			if (linea.getIdEstadoInventario() == null) {
				errores.add(errorAtributoMapeableNoHomologado(model, "BODEGA", linea.getEstadoInventarioCodigoAlterno(),
						MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, asArg(linea)));
			}
		});
	}

	protected String[] asArg(SolicitudDespachoLineaDto linea, String... args) {
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

	protected ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(SolicitudDespachoDto entry, String codigo,
			String... arg) {
		val mensaje = "Este atributo no admite valores nulos o vacios";
		val result = getErroresService().error(entry, codigo, mensaje, arg);
		return result;
	}

	protected ErrorIntegracionDto errorAtributoNoHomologado(SolicitudDespachoDto entry, String codigo, String valor,
			String... arg) {
		val format = "Este atributo requiere ser homologado. Contiene el valor [%s], pero este valor no pudo ser homologado.";
		val mensaje = String.format(format, valor);
		val result = getErroresService().error(entry, codigo, mensaje, arg);
		return result;
	}

	protected ErrorIntegracionDto errorAtributoMapeableNoHomologado(SolicitudDespachoDto entry, String codigo,
			String valor, long idMapa, String... arg) {
		val format = "Este atributo esta asociado al mapa de homologación con id=%d.Verifique que el valor [%s] exista en dicho mapa.";
		val mensaje = String.format(format, idMapa, valor);
		val result = getErroresService().error(entry, codigo, mensaje, arg);
		return result;
	}
}
