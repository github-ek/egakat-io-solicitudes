package com.egakat.io.gws.service.impl.documentos;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.impl.rest.RestPullServiceImpl;
import com.egakat.io.gws.client.constants.RestConstants;
import com.egakat.io.gws.client.constants.SolicitudEstadoConstants;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoSolicitudPullServiceImpl extends RestPullServiceImpl<Integer>
		implements DocumentoSolicitudPullService {

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	//@Autowired
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
		return RestConstants.SOLICITUDES_DESPACHO;
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
	protected ActualizacionDto asModel(String correlacion, Integer input) {
		val result = new ActualizacionDto();

		result.setIntegracion(getIntegracion());
		result.setIdExterno(input.toString());
		result.setCorrelacion(correlacion);
		result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);

		return result;
	}
}
