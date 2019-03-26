package com.egakat.io.clientes.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.enums.EstadoNotificacionType;
import com.egakat.integration.service.impl.rest.RestPushServiceImpl;
import com.egakat.io.clientes.gws.components.GwsRestClient;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.RestConstants;
import com.egakat.io.clientes.gws.constants.SolicitudDespachoClienteEstadoConstants;
import com.egakat.io.clientes.gws.properties.SolicitudesDespachoRestProperties;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionReciboPushService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SolicitudesDespachoNotificacionReciboPushServiceImpl
		extends RestPushServiceImpl<ActualizacionDto, Object, Object>
		implements SolicitudesDespachoNotificacionReciboPushService {

	@Autowired
	private SolicitudesDespachoRestProperties properties;

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
	protected String getOperacion() {
		val result = String.format("PUSH NOTIFICACION RECIBO %s", getIntegracion());
		return result;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;
		val subestado = "";

		val result = getActualizacionesService()
				.findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(getIntegracion(), estado, subestado);
		return result;
	}

	@Override
	protected ActualizacionDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return actualizacion;
	}

	@Override
	protected Object asOutput(ActualizacionDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		return "";
	}

	@Override
	protected Object push(Object output, ActualizacionDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = "/{id}?status={status}";
		val id = actualizacion.getIdExterno();
		val status = SolicitudDespachoClienteEstadoConstants.RECIBIDA_OPL;

		getRestClient().put(url + query, output, Object.class, id, status);
		return "";
	}

	@Override
	protected void onSuccess(Object result, Object output, ActualizacionDto input, ActualizacionDto actualizacion) {
		val estadoNotificacion = EstadoNotificacionType.NOTIFICADA;
		val subestado = SolicitudDespachoClienteEstadoConstants.RECIBIDA_OPL;

		actualizacion.setEstadoNotificacion(estadoNotificacion);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(Object result, Object output, ActualizacionDto input,
			ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estadoNotificacion = EstadoNotificacionType.ERROR;

		actualizacion.setEstadoNotificacion(estadoNotificacion);
	}

	@Override
	protected void updateOnError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			getActualizacionesService().updateErrorNotificacion(actualizacion, errores);
		} catch (RuntimeException e) {
			log.error("Exception:", e);
		}
	}
}