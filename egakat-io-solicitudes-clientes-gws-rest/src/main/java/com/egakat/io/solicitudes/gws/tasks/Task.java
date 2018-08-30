package com.egakat.io.solicitudes.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.egakat.io.solicitudes.gws.service.api.IntegrationService;
import com.egakat.io.solicitudes.gws.service.api.SolicitudDespachoDataQualityService;
import com.egakat.io.solicitudes.gws.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.wms.maestros.client.service.api.OrdStageLocalService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	private SolicitudDespachoDataQualityService dqService;

	@Autowired
	private OrdStageLocalService ordStageService;

	@Autowired
	private SolicitudDespachoCrudService solicitudService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		download();
		transformar();
		upload();
	}

	protected void download() {
		integrationService.execute();
	}

	protected void transformar() {
		val correlaciones = dqService.getCorrelacionesPendientes();
		for (val correlacion : correlaciones) {
			dqService.transformar(correlacion);
		}
	}

	private void upload() {
		try {
			val ordenes = ordStageService.findAllEnStage();

			for (val ord : ordenes) {
				try {
					solicitudService.upload(ord);
				} catch (Exception e) {
					if (e instanceof HttpClientErrorException) {
						val body = ((HttpClientErrorException) e).getResponseBodyAsString();
						log.error("error:{}", body);
					} else {
						log.error("", e);
					}
				}
			}
		} catch (Exception e) {
			if (e instanceof HttpClientErrorException) {
				val body = ((HttpClientErrorException) e).getResponseBodyAsString();
				log.error("error:{}", body);
			} else {
				log.error("", e);
			}
		}
	}
}
