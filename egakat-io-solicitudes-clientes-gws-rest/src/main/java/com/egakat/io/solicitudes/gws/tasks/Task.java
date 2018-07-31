package com.egakat.io.solicitudes.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.solicitudes.gws.service.api.IntegrationService;

@Component
public class Task {

	@Autowired
	private IntegrationService integrationService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		integrationService.execute();
	}
}
