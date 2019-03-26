package com.egakat.io.clientes.gws.service.impl.solicitudes;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.enums.EstadoNotificacionType;
import com.egakat.integration.service.impl.rest.RestPullServiceImpl;
import com.egakat.io.clientes.gws.components.GwsRestClient;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.RestConstants;
import com.egakat.io.clientes.gws.constants.SolicitudDespachoClienteEstadoConstants;
import com.egakat.io.clientes.gws.properties.SolicitudesDespachoRestProperties;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SolicitudesDespachoPullServiceImpl extends RestPullServiceImpl<Integer>
		implements SolicitudesDespachoPullService {

	@Autowired
	private SolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

	@Override
	protected SolicitudesDespachoRestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return RestConstants.SOLICITUDES_DESPACHO_BY_STATUS;
	}

	@Override
	public void pull() {
		val operacion = getOperacion();
		val correlacion = defaultCorrelacion();
		val url = getUrl();
		val query = getQuery();
		val status = SolicitudDespachoClienteEstadoConstants.ENVIAR;
		//al status = "RECIBIDA_OPL1";
		int total = 0;
		String format = "integracion={}, operación= {} ,url= {}{}";

		log.debug(format, getIntegracion(), operacion, url, query);
		try {
			val response = getRestClient().getAllQuery(url, query, Integer[].class, status);
			val inputs = Arrays.asList(response.getBody());
			total = inputs.size();

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			boolean ignorar = isRetryableException(e);
			getErroresService().create(getIntegracion(), correlacion, "", ignorar, e);
			log.error("Exception:", e);
		}

		format = "integracion={}, operación= {}: Finalización de la consulta de solicitudes con estado={}, total={}, url={}{}";
		log.debug(format, getIntegracion(), operacion, status, total, url, query);
	}

	@Override
	protected ActualizacionDto asModel(String correlacion, Integer input) {
		val result = new ActualizacionDto();

		result.setIntegracion(getIntegracion());
		result.setCorrelacion(correlacion);
		result.setIdExterno(input.toString());
		result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
		result.setSubEstadoIntegracion("");
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);

		return result;
	}
}
