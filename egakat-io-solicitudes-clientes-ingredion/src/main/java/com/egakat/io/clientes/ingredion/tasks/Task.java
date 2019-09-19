package com.egakat.io.clientes.ingredion.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.clientes.ingredion.service.api.IngredionSolicitudDespachoMapService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private IngredionSolicitudDespachoMapService mapService;

	private Integer retries = 10;

	private Long delayBetweenRetries = 10L * 1000L;

	@Scheduled(cron = "${cron.solicitudes}")
	public void run() {
		for (int i = 0; i < retries; i++) {
			log.debug("INTEGRACION {}: intento {} de {}", "ACTAS INGREDION", i + 1, retries);
			boolean success = true;

			success &= mapService.map();

			if (success) {
				break;
			} else {
				sleep();
			}
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(delayBetweenRetries * 1000);
		} catch (InterruptedException e) {
			;
		}
	}
}
