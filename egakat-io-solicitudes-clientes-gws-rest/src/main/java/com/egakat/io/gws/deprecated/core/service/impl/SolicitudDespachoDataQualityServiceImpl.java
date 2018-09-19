package com.egakat.io.gws.deprecated.core.service.impl;

import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.CORREGIDO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static com.egakat.io.gws.configuration.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.maps.client.service.api.MapaLocalService;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.gws.deprecated.core.service.api.SolicitudDespachoDataQualityService;

import lombok.val;

@Service
public class SolicitudDespachoDataQualityServiceImpl implements SolicitudDespachoDataQualityService {

	protected static long MAPA_SERVICIO_CODIGO_ALTERNO = 200;
	protected static long MAPA_CANAL_CODIGO_ALTERNO = 201;
	protected static long MAPA_CIUDAD_CODIGO_ALTERNO = 209;
	protected static long MAPA_BODEGA_CODIGO_ALTERNO = 202;
	protected static long MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO = 203;

	protected static List<EstadoIntegracionType> ESTADOS_REGISTROS = asList(ESTRUCTURA_VALIDA, CORREGIDO);

	@Autowired
	private SolicitudDespachoCrudService solicitudService;


	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private MapaLocalService mapaLocalService;

	@Autowired
	private LookUpService lookUpService;

	protected String getIntegracion() {
		return SOLICITUDES_SALIDAS;
	}

	protected List<EstadoIntegracionType> getEstadosIntegracion() {
		return ESTADOS_REGISTROS;
	}

	protected MapaLocalService getMapaService() {
		return mapaLocalService;
	}

	protected LookUpService getLookUpService() {
		return lookUpService;
	}

	@Override
	public void cacheEvict() {
		getLookUpService().cacheEvict();
	}

	@Override
	public List<String> getCorrelacionesPendientes() {
		val result = actualizacionesService.findAllCorrelacionesByEstadoIntegracionIn(getEstadosIntegracion());
		return result;
	}

	@Override
	public void transformar(String correlacion) {
		val actualizaciones = actualizacionesService.findAllByEstadoIntegracionIn(getIntegracion(), ESTRUCTURA_VALIDA,
				CORREGIDO);

		for (val a : actualizaciones) {
			val model = solicitudService.findOneByIntegracionAndIdExterno(a.getIntegracion(), a.getIdExterno());

			val errores = new ArrayList<ErrorIntegracionDto>();
			val entry = actualizacionesService.findOneByIntegracionAndIdExterno(model.getIntegracion(),
					model.getIdExterno());
			val lineas = model.getLineas();

			transformar(model, lineas, errores);
			if (errores.isEmpty()) {
				validar(model, lineas, errores);
			}

			if (errores.isEmpty()) {
				entry.setEstadoIntegracion(EstadoIntegracionType.VALIDADO);
			} else {
				entry.setEstadoIntegracion(EstadoIntegracionType.ERROR_VALIDACION);
				entry.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
			}

			solicitudService.update(model);
			actualizacionesService.update(entry);
			erroresService.create(errores);
		}
	}

	protected void transformar(SolicitudDespachoDto entry, List<SolicitudDespachoLineaDto> lineas,
			List<ErrorIntegracionDto> errores) {
		errores.clear();

		try {

			translateCliente(entry);
			translateServicio(entry);
			translateTercero(entry);
			translateCanal(entry);
			translateCiudad(entry);
			translatePunto(entry);

			lineas.parallelStream().forEach(linea -> {
				translateProducto(entry, linea);
				translateBodega(entry, linea);
				translateEstadoInventario(entry, linea);
			});
		} catch (RuntimeException e) {
			errores.add(error(entry, e));
		}
	}

	protected void validar(SolicitudDespachoDto entry, List<SolicitudDespachoLineaDto> lineas,
			List<ErrorIntegracionDto> errores) {
		errores.clear();

		if (entry.getIdCliente() == null) {
			errores.add(errorAtributoNoHomologado(entry, "CLIENTE", entry.getClienteCodigoAlterno()));
		}

		if (entry.getIdServicio() == null) {
			errores.add(errorAtributoMapeableNoHomologado(entry, "SERVICIO", entry.getServicioCodigoAlterno(),
					MAPA_SERVICIO_CODIGO_ALTERNO));
		}

		if (StringUtils.isEmpty(entry.getNumeroSolicitud())) {
			errores.add(errorAtributoRequeridoNoSuministrado(entry, "NUMERO_SOLICITUD"));
		}

		if (entry.getIdCanal() == null) {
			errores.add(errorAtributoMapeableNoHomologado(entry, "CANAL", entry.getCanalCodigoAlterno(),
					MAPA_CANAL_CODIGO_ALTERNO));
		}

		if (entry.isRequiereTransporte()) {
			if (entry.getIdCiudad() == null) {
				errores.add(errorAtributoNoHomologado(entry, "CIUDAD", entry.getCiudadCodigoAlterno()));
			}

			if (StringUtils.isEmpty(entry.getDireccion())) {
				errores.add(errorAtributoRequeridoNoSuministrado(entry, "DIRECCION"));
			}
		}

		// TODO VALIDAR ID punto
		lineas.parallelStream().forEach(linea -> {
			// TODO VALIDAR LINEAS Y SUBLINEAS EXTERNAS UNICAS
			if (linea.getIdProducto() == null) {
				errores.add(
						errorAtributoNoHomologado(entry, "PRODUCTO", linea.getProductoCodigoAlterno(), asArg(linea)));
			}
			if (linea.getIdBodega() == null) {
				val error = errorAtributoMapeableNoHomologado(entry, "BODEGA", linea.getBodegaCodigoAlterno(),
						MAPA_BODEGA_CODIGO_ALTERNO, asArg(linea));
				error.setArg4(linea.getBodegaCodigoAlterno());
				errores.add(error);
			}
			if (linea.getIdEstadoInventario() == null) {
				errores.add(errorAtributoMapeableNoHomologado(entry, "BODEGA", linea.getEstadoInventarioCodigoAlterno(),
						MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, asArg(linea)));
			}
		});

		// TODO agregar argumentos
		// if (!errores.isEmpty()) {
		// discard(external, errores);
		// }
	}

	protected void translateCliente(SolicitudDespachoDto registro) {
		String key = defaultKey(registro.getClienteCodigoAlterno());

		registro.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(key);
		registro.setIdCliente(id);
	}

	protected void translateServicio(SolicitudDespachoDto registro) {
		String key = defaultKey(registro.getServicioCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_SERVICIO_CODIGO_ALTERNO, key);

		registro.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(key);
		registro.setIdServicio(id);
	}

	protected void translateTercero(SolicitudDespachoDto registro) {
		registro.setIdTercero(null);

		val cliente = registro.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(registro.getTerceroIdentificacion());

			val id = getLookUpService().findTerceroIdByIdAndNumeroIdentificacion(cliente.longValue(), key);
			registro.setIdTercero(id);
		}
	}

	protected void translateCanal(SolicitudDespachoDto registro) {
		String key = defaultKey(registro.getCanalCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_CANAL_CODIGO_ALTERNO, key);

		registro.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(key);
		registro.setIdCanal(id);
	}

	protected void translateCiudad(SolicitudDespachoDto registro) {
		String key = defaultKey(registro.getCiudadCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_CIUDAD_CODIGO_ALTERNO, key);

		registro.setIdCiudad(null);
		Long id = getLookUpService().findCiudadIdByNombreAlterno(key);
		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo(key);
		}
		registro.setIdCiudad(id);
	}

	protected void translatePunto(SolicitudDespachoDto registro) {
		registro.setIdPunto(null);
		val tercero = registro.getIdTercero();
		if (tercero != null) {
			String key = defaultKey(registro.getPuntoCodigoAlterno());

			val id = getLookUpService().findPuntoIdByTerceroIdAndPuntoCodigo(tercero.longValue(), key);
			registro.setIdPunto(id);
		}
	}

	protected void translateProducto(SolicitudDespachoDto registro, SolicitudDespachoLineaDto linea) {
		linea.setIdProducto(null);
		val cliente = registro.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(linea.getProductoCodigoAlterno());

			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), key);
			linea.setIdProducto(id);
		}
	}

	protected void translateBodega(SolicitudDespachoDto registro, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getBodegaCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_BODEGA_CODIGO_ALTERNO, key);

		linea.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		linea.setIdBodega(id);
	}

	protected void translateEstadoInventario(SolicitudDespachoDto registro, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getEstadoInventarioCodigoAlterno());
		key = getValueFromMapOrDefault(MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, key);

		linea.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(key);
		linea.setIdEstadoInventario(id);
	}

	final protected String getValueFromMapOrDefault(Long id, String _default) {
		String result = _default;
		if (id != null) {
			result = getMapaService().findMapaValorByMapaIdAndMapaClave(id, _default);
			if (result == null) {
				result = _default;
			}
		}
		return result;
	}

	protected String defaultKey(String _default) {
		return StringUtils.defaultString(_default).toUpperCase();
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
		val result = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), codigo,
				mensaje, arg);
		return result;
	}

	protected ErrorIntegracionDto errorAtributoNoHomologado(SolicitudDespachoDto entry, String codigo, String valor,
			String... arg) {
		val format = "Este atributo requiere ser homologado. Contiene el valor [%s], pero este valor no pudo ser homologado.";
		val mensaje = String.format(format, valor);
		val result = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), codigo,
				mensaje, arg);
		return result;
	}

	protected ErrorIntegracionDto errorAtributoMapeableNoHomologado(SolicitudDespachoDto entry, String codigo,
			String valor, long idMapa, String... arg) {
		val format = "Este atributo esta asociado al mapa de homologación con id=%d.Verifique que el valor [%s] exista en dicho mapa.";
		val mensaje = String.format(format, idMapa, valor);
		val result = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), codigo,
				mensaje, arg);
		return result;
	}

	protected ErrorIntegracionDto error(SolicitudDespachoDto entry, RuntimeException e) {
		val result = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "", e);
		return result;
	}

	protected void discard(SolicitudDespachoDto entry, List<ErrorIntegracionDto> errores) {
		if (entry != null) {
			for (val error : errores) {
				error.setArg0(entry.getClienteCodigoAlterno());
				error.setArg1(entry.getPrefijo());
				error.setArg2(entry.getNumeroSolicitudSinPrefijo());
				error.setArg3(entry.getServicioCodigoAlterno());
				error.setArg4(entry.getTerceroIdentificacion());
				error.setArg5(entry.getTerceroNombre());
				error.setArg6(entry.getCanalCodigoAlterno());
				error.setArg7(entry.getCiudadCodigoAlterno());
			}
		}
	}

}
