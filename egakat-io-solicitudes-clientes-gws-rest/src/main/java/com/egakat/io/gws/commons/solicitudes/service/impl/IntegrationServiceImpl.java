package com.egakat.io.gws.commons.solicitudes.service.impl;

import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.CORREGIDO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.DESCARTADO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_CARGUE;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_ENRIQUECIMIENTO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_HOMOLOGACION;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ERROR_VALIDACION;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.NO_PROCESADO;
import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.PROCESADO;
import static com.egakat.io.gws.configuration.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.service.api.DownloadService;
import com.egakat.io.gws.commons.core.service.api.NotificationService;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.solicitudes.service.api.IntegrationService;
import com.egakat.io.gws.commons.solicitudes.service.api.SolicitudesDespachoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

	private static final String OPERACION_TAKE_FEEDS = "TAKE_FEEDS";
	private static final String OPERACION_DOWNLOAD = "DOWNLOAD";
	private static final String OPERACION_ACKNOWLEDGE = "ACKNOWLEDGE";
	private static final String OPERACION_ACCEPT = "ACCEPT";
	private static final String OPERACION_REJECT = "REJECT";

	@Autowired
	private SolicitudesDespachoPullService pullService;

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	public String getIntegracion() {
		return SOLICITUDES_SALIDAS;
	}

	protected SolicitudesDespachoPullService getPullService() {
		return pullService;
	}

	protected DownloadService getDownloadService() {
		return downloadService;
	}

	protected NotificationService getNotificationService() {
		return notificationService;
	}

	protected ActualizacionIntegracionCrudService getActualizacionesService() {
		return actualizacionesService;
	}

	protected ErrorIntegracionCrudService getErroresService() {
		return erroresService;
	}

	@Override
	public void execute() {
		pull();
		download();
		//ack();
		//reject();
		//accept();
	}

	protected void pull() {
		try {
			getPullService().pull();
		} catch (RuntimeException e) {
			log(null, OPERACION_TAKE_FEEDS, e);
		}
	}

	protected void download() {
		val entries = getActualizacionesService().findAllByEstadoIntegracionIn(getIntegracion(), NO_PROCESADO,
				CORREGIDO);

		val errores = new ArrayList<ErrorIntegracionDto>();
		for (val entry : entries) {
			try {
				getDownloadService().download(entry, errores);
				getActualizacionesService().updateEstadoIntegracion(entry, errores, ESTRUCTURA_VALIDA,
						ERROR_ESTRUCTURA);
			} catch (RuntimeException e) {
				log(entry, OPERACION_DOWNLOAD, e);
			}
		}
	}

	protected void ack() {
		val entries = getActualizacionesService().findAllNoNotificadasByEstadoIntegracionIn(getIntegracion(),
				ESTRUCTURA_VALIDA);

		for (val entry : entries) {
			try {
				getNotificationService().ack(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_ACKNOWLEDGE, e);
			}
		}
	}

	protected void reject() {
		// @formatter:off
		val entries = getActualizacionesService().findAllNoNotificadasByEstadoIntegracionIn(getIntegracion(), 
		ERROR_ESTRUCTURA,
	    ERROR_ENRIQUECIMIENTO,
	    ERROR_HOMOLOGACION,
	    ERROR_VALIDACION,
	    ERROR_CARGUE,
	    DESCARTADO);
		// @formatter:on

		for (val entry : entries) {
			try {
				getNotificationService().reject(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_REJECT, e);
			}
		}
	}

	protected void accept() {
		val entries = getActualizacionesService().findAllNoNotificadasByEstadoIntegracionIn(getIntegracion(),
				PROCESADO);

		for (val entry : entries) {
			try {
				getNotificationService().accept(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_ACCEPT, e);
			}
		}
	}

	private void log(ActualizacionIntegracionDto entry, String operacion, RuntimeException e) {
		log.error("INTEGRACIÓN:{} ", getIntegracion());
		log.error("OPERACIÓN:{} ", operacion);
		if (entry != null) {
			log.error("ENTRADA:{}", entry.getIdExterno());
		}
		log.error("Excepción:", e);
	}
}
