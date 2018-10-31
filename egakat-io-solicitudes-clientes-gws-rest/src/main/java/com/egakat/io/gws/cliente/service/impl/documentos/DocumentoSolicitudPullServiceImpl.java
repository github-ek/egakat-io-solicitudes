package com.egakat.io.gws.cliente.service.impl.documentos;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;
import com.egakat.core.io.stage.service.impl.RestPullServiceImpl;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.gws.cliente.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.cliente.configuration.constants.SolicitudEstadoConstants;
import com.egakat.io.gws.cliente.configuration.properties.SolicitudesClienteRestProperties;
import com.egakat.io.gws.cliente.service.api.documentos.DocumentoSolicitudPullService;
import com.egakat.io.gws.commons.configuration.constants.IntegracionesConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoSolicitudPullServiceImpl extends RestPullServiceImpl<Integer>
		implements DocumentoSolicitudPullService {

	@Autowired
	private SolicitudesClienteRestProperties properties;

	@Autowired
	private RestClient restClient;

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
		return IntegracionesConstants.DOCUMENTOS_SOLICITUDES_DESPACHO;
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
		val status = SolicitudEstadoConstants.DOC_CREADO_SAP;

		try {
			val inputs = pull(status);

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			getErroresService().create(getIntegracion(), correlacion, "", e);
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
		val result = new ActualizacionIntegracionDto();

		result.setIntegracion(getIntegracion());
		result.setIdExterno(input.toString());
		result.setCorrelacion(correlacion);
		result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);

		return result;
	}
}
