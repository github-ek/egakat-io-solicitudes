package com.egakat.io.gws.cliente.service.impl.ordenes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.cliente.service.api.ordenes.OrdenesAlistamientoPullService;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.impl.PullServiceImpl;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.wms.maestros.client.configuration.properties.WmsRestProperties;
import com.egakat.wms.maestros.configuration.constants.RestConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenesAlistamientoPullServiceImpl extends PullServiceImpl<Integer, SolicitudDespachoDto>
		implements OrdenesAlistamientoPullService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private WmsRestProperties properties;

	@Autowired
	private RestClient restClient;

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_SALIDA_EN_STAGE;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.ordenes_alistamiento + RestConstants.ordenes_alistamiento_suscripciones;
	}

	@Override
	protected String getQuery() {
		return "";
	}

	@Override
	public void pull() {
		val correlacion = defaultCorrelacion();

		try {
			val inputs = pullSuscripciones();

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			getErroresService().create(getIntegracion(), correlacion, "", "", e);
			log.error("Exception:", e);
		}
	}

	protected List<Integer> pullSuscripciones(Object... arg) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getAllQuery(url, query, Integer[].class, arg);
		val result = Arrays.asList(response.getBody());
		return result;
	}

	@Override
	protected ActualizacionIntegracionDto asModel(String correlacion, Integer input) {
		// @formatter:off
		val result = ActualizacionIntegracionDto
				.builder()
				.integracion(getIntegracion())
				.idExterno(input.toString())
				.correlacion(correlacion)
				.estadoIntegracion(EstadoIntegracionType.NO_PROCESADO)
				.estadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD)
				.build();				
		// @formatter:on

		return result;
	}

}
