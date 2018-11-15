package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.client.constants.SolicitudEstadoConstants;

import lombok.val;

@Service
public class SolicitudesDespachoNotificacionReciboPushServiceImpl
		extends SolicitudesDespachoNotificacionPushServiceImpl<String> {

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.ESTRUCTURA_VALIDA, "");
		return result;
	}

	@Override
	protected String asOutput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return "";
	}

	@Override
	protected Object push(String output, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = "/{id}?status={status}";

		getRestClient().put(url + query, output, Object.class, actualizacion.getIdExterno(),
				SolicitudEstadoConstants.RECIBIDA_OPL);
		return "";
	}

	@Override
	protected void onSuccess(Object response, String output, ActualizacionDto actualizacion) {
		super.onSuccess(response, output, actualizacion);
		actualizacion.setSubEstadoIntegracion(SolicitudEstadoConstants.RECIBIDA_OPL);
	}
}
