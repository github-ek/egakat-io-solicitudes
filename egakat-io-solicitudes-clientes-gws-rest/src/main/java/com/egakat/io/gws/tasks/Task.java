package com.egakat.io.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoPullService;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoConfirmarReciboPushServiceImpl;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoMapServiceImpl;

@Component
public class Task {

	@Autowired
	private SolicitudesDespachoPullService solicitudesPullService;

	@Autowired
	private SolicitudesDespachoDownloadService solicitudesDownloadService;

	@Autowired
	private SolicitudesDespachoConfirmarReciboPushServiceImpl solicitudesConfirmarReciboPushService;

	@Autowired
	private SolicitudesDespachoMapServiceImpl solicitudesMapService;

	@Scheduled(cron = "${schedule.start}")
	public void run() {
		descargarSolicitudes();
		notificarAceptacionRechazoDeSolicitudes();
		generarOrdenesDeAlistamiento();
		detectarCierreDeOrdenesDeAlistamiento();
		notificarCierreDeOrdenesDeAlistamiento();
		
		descargarDocumemtosDeEntrega();
		descargarDocumentosDigitales();
		
		distribuirDocumentosDigitales();
		detectarInicioDeRecorrido();
		notificarInicioDeRecorrido();
		detectarFinalizacionDeRecorrido();
		generarOrdenesDeReciboPorRechazo();
		detectarCierreOrdenesDeRecibo();
		notificarCierreDeSolicitudes();
	}

	private void descargarSolicitudes() {
		solicitudesPullService.pull();
		solicitudesDownloadService.download();
		solicitudesConfirmarReciboPushService.push();
		solicitudesMapService.map();
	}

	private void notificarAceptacionRechazoDeSolicitudes() {

	}

	private void generarOrdenesDeAlistamiento() {

	}

	private void detectarCierreDeOrdenesDeAlistamiento() {

	}

	private void notificarCierreDeOrdenesDeAlistamiento() {

	}

	private void descargarDocumemtosDeEntrega() {

	}

	private void descargarDocumentosDigitales() {

	}

	private void distribuirDocumentosDigitales() {

	}

	private void detectarInicioDeRecorrido() {

	}

	private void notificarInicioDeRecorrido() {

	}

	private void detectarFinalizacionDeRecorrido() {

	}

	private void generarOrdenesDeReciboPorRechazo() {

	}

	private void detectarCierreOrdenesDeRecibo() {

	}

	private void notificarCierreDeSolicitudes() {

	}
}
