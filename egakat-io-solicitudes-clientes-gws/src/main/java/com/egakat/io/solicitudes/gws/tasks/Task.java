package com.egakat.io.solicitudes.gws.tasks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.gws.service.api.AbastecimientosInputService;
import com.egakat.io.solicitudes.gws.service.api.SalidasInputService;
import com.egakat.io.solicitudes.gws.service.api.SalidasTapInputService;
import com.egakat.io.solicitudes.gws.service.api.SalidasTatInputService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private SalidasInputService salidasService;

	@Autowired
	private SalidasTapInputService salidasTapService;

	@Autowired
	private SalidasTatInputService salidasTatService;

	@Autowired
	private AbastecimientosInputService abastecimientosService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		// @formatter:off
		val services = Arrays.asList(
				salidasService,
				salidasTapService,
				salidasTatService,
				abastecimientosService);
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
