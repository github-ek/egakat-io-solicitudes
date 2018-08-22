package com.egakat.io.solicitudes.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.gws.service.api.IntegrationService;
import com.egakat.io.solicitudes.gws.service.api.SolicitudDespachoDataQualityService;

import lombok.val;

@Component
public class Task {

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	private SolicitudDespachoDataQualityService dqService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		download();
		transformar();
	}

	protected void download() {
		//integrationService.execute();
	}

	protected void transformar() {
		val correlaciones = dqService.getCorrelacionesPendientes();
		for (val correlacion : correlaciones) {
			dqService.transformar(correlacion);
		}
	}
}
