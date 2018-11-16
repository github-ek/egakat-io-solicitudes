package com.egakat.io.gws.service.impl.ordenes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.SuscripcionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.rest.RestPushNotificationServiceImpl;
import com.egakat.wms.ordenes.client.components.WmsOrdenesRestClient;
import com.egakat.wms.ordenes.client.properties.WmsOrdenesRestProperties;
import com.egakat.wms.ordenes.constants.RestConstants;
import com.egakat.wms.ordenes.constants.SuscripcionesContants;

import lombok.val;

@Service
public class OrdenesAlistamientoCreacionSuscripcionPushServiceImpl
		extends RestPushNotificationServiceImpl<SuscripcionDto, Object> {

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
				getIntegracion(), EstadoIntegracionType.NO_PROCESADO, "");
		return result;
	}

	@Override
	protected SuscripcionDto asOutput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val result = new SuscripcionDto();
		
		result.setSuscripcion(SuscripcionesContants.ORDENES_DE_ALISTAMIENTO);
		result.setIdExterno(actualizacion.getIdExterno());
		result.setEstadoSuscripcion("");
		result.setArg0(actualizacion.getArg0());
		result.setArg1(actualizacion.getArg1());
		result.setArg2(actualizacion.getArg2());
		result.setArg3(actualizacion.getArg3());
		result.setArg4(actualizacion.getArg4());

		return result;
	}

	@Override
	protected Object push(SuscripcionDto output, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();

		getRestClient().post(url, output, Object.class);
		return "";
	}

	@Override
	protected void onSuccess(Object response, SuscripcionDto output, ActualizacionDto actualizacion) {
		actualizacion.setSubEstadoIntegracion("ESPERANDO_CONFIRMACION_CREACION");
		actualizacion.setReintentos(0);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setSubEstadoIntegracion("ERROR_CREANDO_SUSCRIPCION_CREACION");
		actualizacion.setReintentos(0);
	}
}