package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.impl.rest.RestNotificationPushServiceImpl;
import com.egakat.io.gws.client.components.GwsRestClient;
import com.egakat.io.gws.client.constants.RestConstants;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.constants.IntegracionesConstants;

public abstract class SolicitudesDespachoNotificacionPushServiceImpl<O>
		extends RestNotificationPushServiceImpl<O, Object> {

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

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
		return RestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected void onSuccess(Object response, O output, ActualizacionDto actualizacion) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICADA);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void onDiscard(O output, ActualizacionDto actualizacion) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.DESCARTADA);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.ERROR);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(Object response, O output, ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected void updateOnDiscard(O output, ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected void updateOnError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		getActualizacionesService().updateNotificacion(actualizacion, errores);
	}

	@Override
	protected void updateOnRetry(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		getActualizacionesService().updateNotificacion(actualizacion, errores);
	}
}