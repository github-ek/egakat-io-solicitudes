package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.core.service.impl.rest.RestPushServiceImpl;
import com.egakat.io.gws.client.components.GwsRestClient;
import com.egakat.io.gws.client.constants.IntegracionesRestConstants;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.constants.IntegracionesConstants;

import lombok.val;

public abstract class SolicitudesDespachoNotificacionPushServiceImpl<O>
		extends RestPushServiceImpl<SolicitudDespachoDto, O, Object> {

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

	@Autowired
	private SolicitudDespachoCrudService crudService;

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
		return IntegracionesRestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected ExtendedIntegracionEntityCrudService<SolicitudDespachoDto> getCrudService() {
		return crudService;
	}

	@Override
	protected SolicitudDespachoDto getModel(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val result = getCrudService().findOneByIntegracionAndIdExterno(actualizacion.getIntegracion(),
				actualizacion.getIdExterno());
		return result;
	}

	@Override
	protected void onSuccess(Object response, O output, SolicitudDespachoDto model,
			ActualizacionDto actualizacion) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICADA);
		actualizacion.setReintentos(0);
	}
	
	@Override
	protected void onDiscard(O output, SolicitudDespachoDto model, ActualizacionDto actualizacion) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.DESCARTADA);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.ERROR);
		actualizacion.setReintentos(0);
	}
	
	@Override
	protected void updateOnSuccess(Object response, O output, SolicitudDespachoDto model,
			ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected void updateOnDiscard(O output, SolicitudDespachoDto model, ActualizacionDto actualizacion) {
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