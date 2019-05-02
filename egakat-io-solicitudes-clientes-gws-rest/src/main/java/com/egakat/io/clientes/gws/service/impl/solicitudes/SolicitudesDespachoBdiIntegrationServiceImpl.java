package com.egakat.io.clientes.gws.service.impl.solicitudes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoBdiIntegrationService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoMapService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionAceptacionPushService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionRechazoPushService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionReciboPushService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionStagePushService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoPullService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SolicitudesDespachoBdiIntegrationServiceImpl implements SolicitudesDespachoBdiIntegrationService {

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private SolicitudesDespachoDownloadService downloadService;

	@Autowired
	private SolicitudesDespachoNotificacionReciboPushService notificacionReciboPushService;

	@Autowired
	private SolicitudesDespachoMapService mapService;

	@Autowired
	private SolicitudesDespachoNotificacionAceptacionPushService notificacionAceptacionPushService;

	@Autowired
	private SolicitudesDespachoNotificacionRechazoPushService notificacionRechazoPushService;
	
	@Autowired
	private SolicitudesDespachoNotificacionStagePushService notificacionStagePushService;

	@Value("${cron-retries}")
	private Integer retries;

	@Value("${cron-delay-between-retries}")
	private Long delayBetweenRetries;

	@Override
	public String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	public void run() {
		pullService.pull();

		for (int i = 0; i < retries; i++) {
			log.debug("INTEGRACION {}: intento {} de {}", getIntegracion(), i + 1, retries);
			boolean success = true;

			success &= downloadService.push();
			success &= notificacionReciboPushService.push();
			success &= mapService.map();

			// Job APP eIntegration Solicitudes
			// Crear solicitudes
			// Crear ordenes de alistamiento a partir de las solicitudes
			// Crear mensajes de alistamiento a partir de las ordenes de alistamiento
			// Enviar mensajes de alistamiento a WMS por medio de transferencia de archivos
			// XML

			success &= notificacionAceptacionPushService.push();
			success &= notificacionStagePushService.push();
			success &= notificacionRechazoPushService.push();

			if (success) {
				break;
			} else {
				sleep();
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(delayBetweenRetries * 1000);
		} catch (InterruptedException e) {
			;
		}
	}
}
