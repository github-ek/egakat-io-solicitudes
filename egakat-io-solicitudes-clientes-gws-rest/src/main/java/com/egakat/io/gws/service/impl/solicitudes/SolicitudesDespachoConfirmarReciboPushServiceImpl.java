package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.core.service.impl.rest.RestPushServiceImpl;
import com.egakat.io.gws.client.components.GwsRestClient;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.configuration.constants.SolicitudEstadoConstants;

import lombok.val;

@Service
public class SolicitudesDespachoConfirmarReciboPushServiceImpl extends RestPushServiceImpl<SolicitudDespachoDto, String, Object> {

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
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.ESTRUCTURA_VALIDA, "");
		return result;
	}

	@Override
	protected SolicitudDespachoDto getModel(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val result = getCrudService().findOneByIntegracionAndIdExterno(actualizacion.getIntegracion(),
				actualizacion.getIdExterno());
		return result;
	}

	@Override
	protected String asOutput(SolicitudDespachoDto model, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		return "";
	}

	@Override
	protected Object push(String output, SolicitudDespachoDto model, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = "/{id}?status={status}";

		getRestClient().put(url + query, output, Object.class, model.getIdExterno(),
				SolicitudEstadoConstants.RECIBIDA_OPL);
		return "";
	}

	@Override
	protected void onSuccess(Object response, String output, SolicitudDespachoDto model,
			ActualizacionDto actualizacion) {
		actualizacion.setReintentos(0);
		actualizacion.setSubEstadoIntegracion(SolicitudEstadoConstants.RECIBIDA_OPL);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setReintentos(0);
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.ERROR);
	}
}
