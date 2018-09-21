package com.egakat.io.gws.cliente.tasks;

import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.DESCARTADO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_CARGUE;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_ENRIQUECIMIENTO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_HOMOLOGACION;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_VALIDACION;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.PROCESADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.egakat.io.gws.cliente.service.api.deprecated.SolicitudDespachoDataQualityService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoNotificationService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoPullService;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SolicitudesDespachoTask {

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private SolicitudesDespachoDownloadService downloadService;

	@Autowired
	private SolicitudesDespachoNotificationService notificationService;

	@Autowired
	private SolicitudDespachoDataQualityService dataQualityService;

	public void run() {
		pull();
		download();
		ack();
		validate();
		reject();
		accept();
	}

	protected void pull() {
		try {
			pullService.pull();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void download() {
		try {
			downloadService.download();
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	protected void validate() {
		try {
			dataQualityService.validate();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void ack() {
		val entries = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(
				IntegracionesConstants.SOLICITUDES_SALIDAS, ESTRUCTURA_VALIDA);

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
		val entries = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(IntegracionesConstants.SOLICITUDES_SALIDAS, 
		ERROR_ESTRUCTURA,
	    ERROR_ENRIQUECIMIENTO,
	    ERROR_HOMOLOGACION,
	    ERROR_VALIDACION,
	    ERROR_CARGUE,
	    DESCARTADO);
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
				.findAllNoNotificadasByEstadoIntegracionIn(IntegracionesConstants.SOLICITUDES_SALIDAS, PROCESADO);

		for (val entry : entries) {
			try {
				notificationService.accept(entry);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}
}
