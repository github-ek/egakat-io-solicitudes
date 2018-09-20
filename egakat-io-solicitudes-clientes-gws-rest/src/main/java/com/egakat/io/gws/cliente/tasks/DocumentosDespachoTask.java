package com.egakat.io.gws.cliente.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;

import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.wms.maestros.client.service.api.OrdStageLocalService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class DocumentosDespachoTask {


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
		//integrationService.execute();
	}

	protected void transformar() {

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
