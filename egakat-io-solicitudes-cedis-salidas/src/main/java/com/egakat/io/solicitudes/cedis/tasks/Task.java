package com.egakat.io.solicitudes.cedis.tasks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.cedis.service.api.DestruccionesInputService;
import com.egakat.io.solicitudes.cedis.service.api.SalidasInputService;
import com.egakat.io.solicitudes.cedis.service.api.AbastecimientosInputService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	// TRASLADOS
	@Autowired
	private AbastecimientosInputService abastecimientosService;

	// SALIDAS
	@Autowired
	private SalidasInputService salidasService;

	@Autowired
	private SalidasInputService crossDockingService;

	@Autowired
	private DestruccionesInputService destruccionesService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		// @formatter:off
		val services = Arrays.asList(
				abastecimientosService,

				salidasService,
				crossDockingService,
				destruccionesService
				
				);
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