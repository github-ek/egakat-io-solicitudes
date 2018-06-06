package com.egakat.io.solicitudes.transformation.tasks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.transformation.service.api.DestruccionesTransformationService;
import com.egakat.io.solicitudes.transformation.service.api.ManufacturasBomTransformationService;
import com.egakat.io.solicitudes.transformation.service.api.ManufacturasTransformationService;
import com.egakat.io.solicitudes.transformation.service.api.RecibosTransformationService;
import com.egakat.io.solicitudes.transformation.service.api.SalidasTransformationService;
import com.egakat.io.solicitudes.transformation.service.api.TrasladosTransformationService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private SalidasTransformationService salidasTransformationService;

	@Autowired
	private TrasladosTransformationService trasladosTransformationService;

	@Autowired
	private DestruccionesTransformationService destruccionesTransformationService;

	@Autowired
	private RecibosTransformationService reciboTransformationService;

	@Autowired
	private ManufacturasTransformationService manufacturasTransformationService;

	@Autowired
	private ManufacturasBomTransformationService manufacturasBomTransformationService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		// @formatter:off
		val services = Arrays.asList(
				salidasTransformationService,
				/*destruccionesTransformationService,*/ 
				/*reciboTransformationService,*/
				trasladosTransformationService,
				manufacturasTransformationService,
				manufacturasBomTransformationService
				);
		// @formatter:on

		services.parallelStream().forEach(service -> {
			val archivos = service.getArchivosPendientes();
			archivos.stream().forEach(archivoId -> {
				try {
					service.transformar(archivoId);
				} catch (RuntimeException e) {
					log.error("service.extraer(" + archivoId + ");", e);
				}
			});
		});
	}
}