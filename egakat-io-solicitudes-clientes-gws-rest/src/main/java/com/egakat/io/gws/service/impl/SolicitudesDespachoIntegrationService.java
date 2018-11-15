package com.egakat.io.gws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoPullService;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoMapServiceImpl;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoNotificacionAceptacionPushServiceImpl;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoNotificacionRechazoPushServiceImpl;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoNotificacionReciboPushServiceImpl;

@Service
public class SolicitudesDespachoIntegrationService {
	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private SolicitudesDespachoDownloadService downloadService;

	@Autowired
	private SolicitudesDespachoNotificacionReciboPushServiceImpl notificacionReciboPushService;

	@Autowired
	private SolicitudesDespachoMapServiceImpl mapService;

	@Autowired
	private SolicitudesDespachoNotificacionAceptacionPushServiceImpl notificacionAceptacionPushService;

	@Autowired
	private SolicitudesDespachoNotificacionRechazoPushServiceImpl notificacionRechazoPushService;

	public void run() {
		pullService.pull();
		downloadService.download();
		notificacionReciboPushService.push();
		mapService.map();

		// Job APP eIntegration Solicitudes
		// Crear solicitudes
		// Crear ordenes de alistamiento a partir de las solicitudes
		// Crear mensajes de alistamiento a partir de las ordenes de alistamiento
		// Enviar mensajes de alistamiento a WMS por medio de transferencia de archivos
		// XML

		notificacionAceptacionPushService.push();
		notificacionRechazoPushService.push();
	}
}
