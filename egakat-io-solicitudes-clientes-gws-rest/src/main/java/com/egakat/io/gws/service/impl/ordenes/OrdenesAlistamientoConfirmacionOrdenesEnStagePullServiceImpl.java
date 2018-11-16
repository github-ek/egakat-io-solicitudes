package com.egakat.io.gws.service.impl.ordenes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.service.impl.rest.RestPullServiceImpl;
import com.egakat.wms.ordenes.client.components.WmsOrdenesRestClient;
import com.egakat.wms.ordenes.client.properties.WmsOrdenesRestProperties;
import com.egakat.wms.ordenes.constants.RestConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenesAlistamientoConfirmacionOrdenesEnStagePullServiceImpl extends RestPullServiceImpl<String> {

	@Autowired
	private WmsOrdenesRestProperties properties;

	@Autowired
	private WmsOrdenesRestClient restClient;

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO;
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
	protected String getApiEndPoint() {
		return RestConstants.suscripciones_ordenes_alistamiento;
	}

	@Override
	protected String getQuery() {
		return RestConstants.suscripciones_ordenes_alistamiento_en_stage;
	}

	@Override
	public void pull() {
		val correlacion = defaultCorrelacion();

		try {
			val format = "integracion={}, correlacion= {}";
			log.debug(format, getIntegracion(), correlacion);

			val inputs = pull("");

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			getErroresService().create(getIntegracion(), correlacion, "", e);
			log.error("Exception:", e);
		}
	}

	protected List<String> pull(Object... arg) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getAllQuery(url, query, String[].class, arg);
		val result = Arrays.asList(response.getBody());
		return result;
	}

	@Override
	protected ActualizacionDto asModel(String correlacion, String input) {
		ActualizacionDto result = null;
		val exists = getActualizacionesService().exists(getIntegracion(), input);

		if (exists) {
			result = getActualizacionesService().findOneByIntegracionAndIdExterno(getIntegracion(), input);
			switch (result.getSubEstadoIntegracion()) {
			case "":
			case "ESPERANDO_CONFIRMACION_STAGE":
				result.setSubEstadoIntegracion("STAGE_CONFIRMADO");
				break;
			default:
				result = null;
				break;
			}
		}

		return result;
	}

	@Override
	protected void updateOnSuccess(String input, ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected boolean shouldBeDiscarded(String input, ActualizacionDto model) {
		if (model == null) {
			return true;
		} else {
			return false;
		}
	}

}
