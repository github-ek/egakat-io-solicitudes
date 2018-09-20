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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.egakat.io.gws.cliente.service.api.NotificationService;
import com.egakat.io.gws.cliente.service.api.SolicitudesDespachoPullService;
import com.egakat.io.gws.cliente.service.api.deprecated.SolicitudDespachoDataQualityService;
import com.egakat.io.gws.commons.core.service.api.DownloadService;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.wms.maestros.client.service.api.OrdStageLocalService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SolicitudesDespachoTask {

	private static final String OPERACION_PULL = "PULL";

	private static final String OPERACION_DOWNLOAD = "DOWNLOAD";

	private static final String OPERACION_ACKNOWLEDGE = "ACK";

	private static final String OPERACION_DATA_QUALITY = "DATA_QUALITY";

	private static final String OPERACION_ACCEPT = "ACCEPT";

	private static final String OPERACION_REJECT = "REJECT";

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SolicitudDespachoDataQualityService dataQualityService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		pull();
		download();
		ack();
		validate();
		reject();
		accept();

		push();
	}

	protected void pull() {
		try {
			pullService.pull();
		} catch (Exception e) {
			log.error("Ocurrio un error durante la operación " + OPERACION_PULL + ":", e);
		}
	}

	protected void download() {
		try {
			downloadService.download();
		} catch (Exception e) {
			log.error("Ocurrio un error durante la operación " + OPERACION_DOWNLOAD + ":", e);
		}
	}

	protected void ack() {
		val entries = actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(
				IntegracionesConstants.SOLICITUDES_SALIDAS, ESTRUCTURA_VALIDA);

		for (val entry : entries) {
			try {
				notificationService.ack(entry);
			} catch (RuntimeException e) {
				log.error("Ocurrio un error durante la operación " + OPERACION_ACKNOWLEDGE + ":", e);
			}
		}
	}

	protected void validate() {
		try {
			dataQualityService.validate();
		} catch (Exception e) {
			log.error("Ocurrio un error durante la operación " + OPERACION_DATA_QUALITY + ":", e);
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
				log.error("Ocurrio un error durante la operación " + OPERACION_REJECT + ":", e);
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
				log.error("Ocurrio un error durante la operación " + OPERACION_ACCEPT + ":", e);
			}
		}
	}

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private OrdStageLocalService ordStageService;

	protected void push() {
		try {
			val ordenes = ordStageService.findAllEnStage();

			for (val ord : ordenes) {
				try {
					crudService.upload(ord);
				} catch (Exception e) {
					if (e instanceof HttpClientErrorException) {
						val body = ((HttpClientErrorException) e).getResponseBodyAsString();
						log.error("error:{}", body);
					} else {
						log.error("", e);
					}
				}
			}
		} catch (Exception e) {
			if (e instanceof HttpClientErrorException) {
				val body = ((HttpClientErrorException) e).getResponseBodyAsString();
				log.error("error:{}", body);
			} else {
				log.error("", e);
			}
		}
	}

}
