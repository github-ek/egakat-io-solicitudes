package com.egakat.io.solicitudes.cedis.tasks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasBomInputService;
import com.egakat.io.solicitudes.cedis.service.api.ManufacturasInputService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private ManufacturasInputService ofertasService;

	@Autowired
	private ManufacturasInputService desensamblesService;

	@Autowired
	private ManufacturasInputService etiquetadosService;

	@Autowired
	private ManufacturasInputService estampilladosService;

	@Autowired
	private ManufacturasInputService desestampilladosService;

	@Autowired
	private ManufacturasBomInputService ofertasBomService;

	@Autowired
	private ManufacturasBomInputService acondicionamientosService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		// @formatter:off
		val services = Arrays.asList(
				acondicionamientosService,
				desensamblesService,
				desestampilladosService,

				estampilladosService,
				etiquetadosService,

				ofertasService,
				ofertasBomService
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
