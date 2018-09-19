package com.egakat.io.gws.cliente.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.gws.cliente.service.api.NotificationService;
import com.egakat.io.gws.cliente.service.api.SolicitudesDespachoPullService;
import com.egakat.io.gws.commons.core.service.api.DownloadService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SolicitudesDespachoTask {

	private static final String OPERACION_PULL = "PULL";

	private static final String OPERACION_DOWNLOAD = "DOWNLOAD";

	private static final String OPERACION_ACKNOWLEDGE = "ACKNOWLEDGE";

	private static final String OPERACION_ACCEPT = "ACCEPT";

	private static final String OPERACION_REJECT = "REJECT";

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private NotificationService notificationService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		pull();
		download();
		// transformar();
		// upload();
	}

	protected void pull() {
		try {
			pullService.pull();
		} catch (Exception e) {
			log.error("Ocurrio un error durante la operación " + OPERACION_PULL + ":", e);
		}
	}

	protected void download() {
		try {
			downloadService.download();
		} catch (Exception e) {
			log.error("Ocurrio un error durante la operación " + OPERACION_DOWNLOAD + ":", e);
		}
	}

	// private void log(ActualizacionIntegracionDto actualizacion, String operacion,
	// RuntimeException e) {
	// log.error("INTEGRACIÓN:{} ", actualizacion.getIntegracion());
	// log.error("CORRELACIÓN:{} ", actualizacion.getCorrelacion());
	// log.error("OPERACIÓN:{} ", operacion);
	// log.error("ENTRADA:{}", actualizacion.getIdExterno());
	// log.error("Excepción:", e);
	// }

	// @Autowired
	// private IntegrationService integrationService;
	//
	// @Autowired
	// private SolicitudDespachoDataQualityService dqService;
	//
	// @Autowired
	// private OrdStageLocalService ordStageService;
	//
	// @Autowired
	// private SolicitudDespachoCrudService solicitudService;
	//
	// protected void transformar() {
	// val correlaciones = dqService.getCorrelacionesPendientes();
	// for (val correlacion : correlaciones) {
	// dqService.transformar(correlacion);
	// }
	// }
	//
	// protected void upload() {
	// try {
	// val ordenes = ordStageService.findAllEnStage();
	//
	// for (val ord : ordenes) {
	// try {
	// solicitudService.upload(ord);
	// } catch (Exception e) {
	// if (e instanceof HttpClientErrorException) {
	// val body = ((HttpClientErrorException) e).getResponseBodyAsString();
	// log.error("error:{}", body);
	// } else {
	// log.error("", e);
	// }
	// }
	// }
	// } catch (Exception e) {
	// if (e instanceof HttpClientErrorException) {
	// val body = ((HttpClientErrorException) e).getResponseBodyAsString();
	// log.error("error:{}", body);
	// } else {
	// log.error("", e);
	// }
	// }
	// }
}
