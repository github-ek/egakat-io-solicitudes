package com.egakat.io.gws.service.impl.ordenes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.rest.RestPushNotificationServiceImpl;
import com.egakat.wms.ordenes.client.components.WmsOrdenesRestClient;
import com.egakat.wms.ordenes.client.properties.WmsOrdenesRestProperties;
import com.egakat.wms.ordenes.constants.RestConstants;

import lombok.val;

@Service
public class OrdenesAlistamientoConfirmacionOrdenesEnStageAckPushNotificacionServiceImpl
		extends RestPushNotificationServiceImpl<Object, Object> {

	@Autowired
	private WmsOrdenesRestProperties properties;

	@Autowired
	private WmsOrdenesRestClient restClient;

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
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.NO_PROCESADO, "STAGE_CONFIRMADO");
		return result;
	}

	@Override
	protected Object asOutput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return "";
	}

	@Override
	protected Object push(Object output, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getUrl() + RestConstants.suscripciones_ordenes_alistamiento_en_stage_ack;

		getRestClient().put(url, output, Object.class, actualizacion.getIdExterno());
		return "";
	}

	@Override
	protected void onSuccess(Object response, Object output, ActualizacionDto actualizacion) {
		actualizacion.setSubEstadoIntegracion("DESCARGAR");
		actualizacion.setReintentos(0);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setSubEstadoIntegracion("ERROR_NOTIFICANDO_ACK_STAGE");
		actualizacion.setReintentos(0);
	}
}