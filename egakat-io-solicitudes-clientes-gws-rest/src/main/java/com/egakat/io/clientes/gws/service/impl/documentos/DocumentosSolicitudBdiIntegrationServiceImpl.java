package com.egakat.io.clientes.gws.service.impl.documentos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudBdiIntegrationService;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudDownloadService;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudNotificacionReciboPushService;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudPullService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentosSolicitudBdiIntegrationServiceImpl implements DocumentosSolicitudBdiIntegrationService {

	@Autowired
	private DocumentosSolicitudPullService pullService;

	@Autowired
	private DocumentosSolicitudDownloadService downloadService;

	@Autowired
	private DocumentosSolicitudNotificacionReciboPushService notificacionReciboPushService;

	@Value("${cron-retries}")
	private Integer retries;

	@Value("${cron-delay-between-retries}")
	private Long delayBetweenRetries;

	@Override
	public String getIntegracion() {
		return IntegracionesConstants.DOCUMENTOS_SOLICITUDES_DESPACHO;
	}

	@Override
	public void run() {
		pullService.pull();

		for (int i = 0; i < retries; i++) {
			log.debug("INTEGRACION {}: intento {} de {}", getIntegracion(), i + 1, retries);
			boolean success = true;

			success &= downloadService.push();
			success &= notificacionReciboPushService.push();

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
