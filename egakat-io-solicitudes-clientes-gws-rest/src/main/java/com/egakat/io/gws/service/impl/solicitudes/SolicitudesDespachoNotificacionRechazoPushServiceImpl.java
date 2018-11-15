package com.egakat.io.gws.service.impl.solicitudes;

import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_CARGUE;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_HOMOLOGACION;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_VALIDACION;

import java.util.List;

import org.springframework.stereotype.Service;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.client.constants.SolicitudEstadoConstants;
import com.egakat.io.gws.constants.IntegracionesConstants;

import lombok.val;

@Service
public class SolicitudesDespachoNotificacionRechazoPushServiceImpl
		extends SolicitudesDespachoNotificacionPushServiceImpl<List<ErrorIntegracionDto>> {

	@Override
	protected List<ActualizacionDto> getPendientes() {
		// @formatter:off
		val result = getActualizacionesService().findAllNoNotificadasByEstadoIntegracionIn(
		IntegracionesConstants.SOLICITUDES_DESPACHO, 
		ERROR_ESTRUCTURA,
	    ERROR_HOMOLOGACION,
	    ERROR_VALIDACION,
	    ERROR_CARGUE);
		// @formatter:on
		return result;
	}

	@Override
	protected List<ErrorIntegracionDto> asOutput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val result = getErroresService().findAll(actualizacion);
		return result;
	}

	@Override
	protected Object push(List<ErrorIntegracionDto> output, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = "/{id}/error?status={status}";

		getRestClient().put(url + query, output, Object.class, actualizacion.getIdExterno(),
				SolicitudEstadoConstants.RECHAZADA_OPL);
		return "";
	}

	@Override
	protected void onSuccess(Object response, List<ErrorIntegracionDto> output, ActualizacionDto actualizacion) {
		super.onSuccess(response, output, actualizacion);
		actualizacion.setSubEstadoIntegracion(SolicitudEstadoConstants.RECHAZADA_OPL);
	}
}
