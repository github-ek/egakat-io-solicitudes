package com.egakat.io.silogtran.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.silogtran.service.api.RemesasPushService;

@Component
public class Task {

	@Autowired
	private RemesasPushService service;
	
	@Scheduled(cron = "${cron-remesas}")
	public void run() {
		//service.push();
	}
}
