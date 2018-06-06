package com.egakat.io.solicitudes.heinz.tasks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.heinz.service.api.SalidasInputService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private SalidasInputService salidasService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		// @formatter:off
		val services = Arrays.asList(
				salidasService);
		// @formatter:on

		services.parallelStream().forEach(service -> {
			val archivos = service.getArchivosPendientes();
			archivos.stream().forEach(archivoId -> {
				try {
					service.extraer(archivoId);
				} catch (RuntimeException e) {
					log.error("service.extraer(" + archivoId + ");", e);
				}
			});
		});
	}
}
