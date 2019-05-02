package com.egakat.io.clientes.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudBdiIntegrationService;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoBdiIntegrationService;

@Component
public class Task {

	@Autowired
	private SolicitudesDespachoBdiIntegrationService solicitudesDespachoService;

	@Autowired
	private DocumentosSolicitudBdiIntegrationService documentosSolicitudService;

	@Scheduled(cron = "${cron.solicitudes}")
	public void run() {
		solicitudesDespachoService.run();
		documentosSolicitudService.run();

		descargarDocumentosDigitales();
	}

	private void descargarDocumentosDigitales() {
		// EC DEBE INICIA UN FLUJO PARA DECARGAR LOS DOCUMENTOS DIGITALES
	}
}
