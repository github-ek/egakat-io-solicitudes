package com.egakat.io.gws.cliente.tasks;

import static com.egakat.core.io.stage.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.cliente.service.api.documentos.DocumentoSolicitudDownloadService;
import com.egakat.io.gws.cliente.service.api.documentos.DocumentoSolicitudNotificationService;
import com.egakat.io.gws.cliente.service.api.documentos.DocumentoSolicitudPullService;
import com.egakat.io.gws.commons.configuration.constants.IntegracionesConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DocumentosDespachoTask {

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private DocumentoSolicitudPullService pullService;

	@Autowired
	private DocumentoSolicitudDownloadService downloadService;

	@Autowired
	private DocumentoSolicitudNotificationService notificationService;
	//
	// @Autowired
	// private SolicitudDespachoDataQualityService dataQualityService;

	public void run() {
		pull();
		download();
		ack();
		// validate();
		// reject();
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
			// dataQualityService.validate();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void ack() {
		val actualizaciones = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(
				IntegracionesConstants.DOCUMENTOS_SOLICITUDES_DESPACHO, ESTRUCTURA_VALIDA);

		for (val actualizacion : actualizaciones) {
			try {
				notificationService.ack(actualizacion);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}

	protected void reject() {

	}

	protected void accept() {

	}

}
