package com.egakat.io.clientes.gws.service.impl.documentos;

import static com.egakat.integration.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.service.api.crud.ActualizacionCrudService;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentoSolicitudDownloadService;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentoSolicitudPullService;
import com.egakat.io.commons.constants.IntegracionesConstants;

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

	//@Autowired
	//private DocumentoSolicitudNotificationService notificationService;
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
			downloadService.push();
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
		val actualizaciones = actualizacionesService.findAllNoNotificadasByIntegracionAndEstadoIntegracionIn(
				IntegracionesConstants.DOCUMENTOS_SOLICITUDES_DESPACHO, ESTRUCTURA_VALIDA);

		for (val actualizacion : actualizaciones) {
			try {
				log.debug(actualizacion.toString());
				//notificationService.notify(actualizacion);
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
