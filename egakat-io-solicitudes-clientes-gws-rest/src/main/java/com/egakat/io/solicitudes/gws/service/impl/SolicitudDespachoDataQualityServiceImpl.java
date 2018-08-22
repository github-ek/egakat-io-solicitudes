package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;
import static com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType.CORREGIDO;
import static com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static java.util.Arrays.asList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.files.client.service.api.TipoArchivoLocalService;
import com.egakat.integration.maps.client.service.api.MapaLocalService;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoLineaDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.service.api.SolicitudDespachoDataQualityService;
import com.egakat.io.solicitudes.gws.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.SolicitudDespachoLineaCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	private SolicitudDespachoLineaCrudService solicitudLineaService;

	@Autowired
	private TipoArchivoLocalService tipoArchivoLocalService;

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

	protected TipoArchivoLocalService getTipoArchivoService() {
		return tipoArchivoLocalService;
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
		val result = solicitudService.findAllCorrelacionesByEstadoIntegracionIn(getEstadosIntegracion());
		return result;
	}

	@Override
	public void transformar(String correlacion) {
		val entries = solicitudService.findAllByCorrelacionAndEstadoIntegracionIn(correlacion, getEstadosIntegracion());

		if (!entries.isEmpty()) {
			int i = 1;
			int n = entries.size();
			val format = "{} de {}:id={} ,integracion={}, id_externo={}, correlacion={}, estado_integracion={}";

			for (val entry : entries) {
				log.debug(format, i++, n, entry.getId(), entry.getIntegracion(), entry.getIdExterno(),
						entry.getCorrelacion(), entry.getEstadoIntegracion());
				transformar(entry);
			}
		}

	}

	protected void transformar(SolicitudDespachoDto entry) {
		val lineas = solicitudLineaService.findAllByIdSolicitudDespacho(entry.getId());

		translateCliente(entry);
		translateServicio(entry);
		translateTercero(entry);
		translateCanal(entry);
		translateCiudad(entry);
		translatePunto(entry);

		for (val linea : lineas) {
			translateProducto(entry, linea);
			translateBodega(entry, linea);
			translateEstadoInventario(entry, linea);
		}
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
}