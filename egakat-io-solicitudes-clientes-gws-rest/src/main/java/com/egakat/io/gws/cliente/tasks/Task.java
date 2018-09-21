package com.egakat.io.gws.cliente.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {

	@Autowired
	private SolicitudesDespachoTask solicitudes;

	@Autowired
	private OrdenesAlistamientoTask ordenes;
	
	@Scheduled(cron = "${schedule.start}")
	public void run() {
		solicitudes.run();
		ordenes.run();
	}
}
