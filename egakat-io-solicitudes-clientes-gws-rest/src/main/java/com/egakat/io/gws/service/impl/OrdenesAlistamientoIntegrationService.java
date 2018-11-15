package com.egakat.io.gws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoSuscripcionPushNotificacionServiceImpl;

@Service
public class OrdenesAlistamientoIntegrationService {
	@Autowired
	private OrdenesAlistamientoPullService pullService;

	@Autowired
	private OrdenesAlistamientoSuscripcionPushNotificacionServiceImpl notificacionCreacionPushService; 
	
	public void run() {
		pullService.pull();
		//notificacionCreacionPushService.push();
	}
}
