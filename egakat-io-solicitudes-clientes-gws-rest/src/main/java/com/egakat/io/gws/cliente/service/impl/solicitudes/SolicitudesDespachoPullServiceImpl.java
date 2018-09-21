package com.egakat.io.gws.cliente.service.impl.solicitudes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.cliente.configuration.constants.SolicitudEstadoConstants;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoPullService;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.impl.PullServiceImpl;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.configuration.properties.SolicitudDespachoClienteRestProperties;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SolicitudesDespachoPullServiceImpl extends PullServiceImpl<Integer, SolicitudDespachoDto>
		implements SolicitudesDespachoPullService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private SolicitudDespachoClienteRestProperties properties;

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
		return IntegracionesConstants.SOLICITUDES_SALIDAS;
	}

	@Override
	protected String getApiEndPoint() {
		return IntegracionesRestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "?status={status}";
	}

	@Override
	public void pull() {
		val correlacion = defaultCorrelacion();
		val status = SolicitudEstadoConstants.ENVIAR;

		try {
			val inputs = pull(status);

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			getErroresService().create(getIntegracion(), correlacion, "", "", e);
			log.error("Exception:", e);
		}
	}

	protected List<Integer> pull(Object... arg) {
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
