package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.CORREGIDO;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.DESCARTADO;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_CARGUE;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_ENRIQUECIMIENTO;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_HOMOLOGACION;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_VALIDACION;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ESTRUCTURA_VALIDA;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.NO_PROCESADO;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.PROCESADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.service.api.NotificationSchedulerService;
import com.egakat.io.solicitudes.gws.service.api.DownloadService;
import com.egakat.io.solicitudes.gws.service.api.IntegrationService;
import com.egakat.io.solicitudes.gws.service.api.TakeFeedsService;
import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import static com.egakat.io.solicitudes.gws.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;

@Service
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

	private static final String OPERACION_TAKE_FEEDS = "TAKE_FEEDS";
	private static final String OPERACION_DOWNLOAD = "DOWNLOAD";
	private static final String OPERACION_ACKNOWLEDGE = "ACKNOWLEDGE";
	private static final String OPERACION_ACCEPT = "ACCEPT";
	private static final String OPERACION_REJECT = "REJECT";

	@Autowired
	private TakeFeedsService takeFeedsService;

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private NotificationSchedulerService nNotificationSchedulerService;

	@Autowired
	private EntradaIntegracionCrudService entradasService;

	@Override
	public String getCodigoIntegracion() {
		return SOLICITUDES_SALIDAS;
	}

	@Override
	public TakeFeedsService getTakeFeedsService() {
		return takeFeedsService;
	}

	@Override
	public DownloadService getDownloadService() {
		return downloadService;
	}

	@Override
	public NotificationSchedulerService getNotificationSchedulerService() {
		return nNotificationSchedulerService;
	}

	@Override
	public EntradaIntegracionCrudService getEntradasService() {
		return entradasService;
	}

	@Override
	public ErrorIntegracionCrudService getErroresService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() {
		takeFeeds();
		download();
		ack();
		reject();
		accept();
		//
		System.out.println();
	}

	protected void takeFeeds() {
		try {
			getTakeFeedsService().takeFeeds();
		} catch (RuntimeException e) {
			log(null, OPERACION_TAKE_FEEDS, e);
		}
	}

	protected void download() {
		val entries = getEntradasService().findAllByIntegracionAndEstado(getCodigoIntegracion(), NO_PROCESADO,
				CORREGIDO);

		for (val entry : entries) {
			try {
				getDownloadService().download(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_DOWNLOAD, e);
			}
		}
	}

	protected void ack() {
		val entries = getEntradasService().findAllByIntegracionAndEstado(getCodigoIntegracion(), true,
				ESTRUCTURA_VALIDA);

		for (val entry : entries) {
			try {
				getNotificationSchedulerService().ack(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_ACKNOWLEDGE, e);
			}
		}
	}

	protected void reject() {
		// @formatter:off
		val entries = getEntradasService().findAllByIntegracionAndEstado(getCodigoIntegracion(), true,
		ERROR_ESTRUCTURA,
	    ERROR_ENRIQUECIMIENTO,
	    ERROR_HOMOLOGACION,
	    ERROR_VALIDACION,
	    ERROR_CARGUE,
	    DESCARTADO);
		// @formatter:on

		for (val entry : entries) {
			try {
				getNotificationSchedulerService().reject(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_REJECT, e);
			}
		}
	}

	protected void accept() {
		val entries = getEntradasService().findAllByIntegracionAndEstado(getCodigoIntegracion(), PROCESADO);

		for (val entry : entries) {
			try {
				getNotificationSchedulerService().accept(entry);
			} catch (RuntimeException e) {
				log(entry, OPERACION_ACCEPT, e);
			}
		}
	}

	private void log(EntradaIntegracionDto entry, String operacion, RuntimeException e) {
		log.error("INTEGRACIÓN:{} ", getCodigoIntegracion());
		log.error("OPERACIÓN:{} ", operacion);
		if (entry != null) {
			log.error("ENTRADA:{}", entry.getIdExterno());
		}
		log.error("Excepción:", e);
	}
}
