package com.egakat.io.gws.components;


import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_CARGUE;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_HOMOLOGACION;
import static com.egakat.io.core.enums.EstadoIntegracionType.ERROR_VALIDACION;
import static com.egakat.io.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static com.egakat.io.core.enums.EstadoIntegracionType.PROCESADO;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoNotificationService;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class SolicitudesDespachoIntegration {

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private SolicitudesDespachoDownloadService downloadService;

	@Autowired
	private SolicitudesDespachoNotificationService notificationService;

	public void run() {

		ack();
		reject();
		accept();
	}

	protected void ack() {
		val entries = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(
				IntegracionesConstants.SOLICITUDES_DESPACHO, ESTRUCTURA_VALIDA);

		for (val entry : entries) {
			try {
				notificationService.ack(entry);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}
	
	protected void reject() {
		// @formatter:off
		val entries = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(IntegracionesConstants.SOLICITUDES_DESPACHO, 
		ERROR_ESTRUCTURA,
	    ERROR_HOMOLOGACION,
	    ERROR_VALIDACION,
	    ERROR_CARGUE);
		// @formatter:on

		for (val entry : entries) {
			try {
				notificationService.reject(entry);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}

	protected void accept() {
		val entries = actualizacionesService
				.findAllNoNotificadasByEstadoIntegracionIn(IntegracionesConstants.SOLICITUDES_DESPACHO, PROCESADO);

		for (val entry : entries) {
			try {
				notificationService.accept(entry);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}
}
