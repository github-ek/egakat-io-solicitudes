package com.egakat.io.gws.components;

import static com.egakat.io.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudDownloadService;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudNotificationService;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class DocumentosDespachoIntegration {

	@Autowired
	private ActualizacionCrudService actualizacionesService;

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
