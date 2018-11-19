package com.egakat.io.gws.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.gws.service.impl.OrdenesAlistamientoIntegrationService;
import com.egakat.io.gws.service.impl.SolicitudesDespachoIntegrationService;

@Component
public class Task {

	@Autowired
	private SolicitudesDespachoIntegrationService solicitudesDespachoService;

	@Autowired
	private OrdenesAlistamientoIntegrationService ordenesAlistamientoIntegrationService; 
	
	
	@Scheduled(cron = "${schedule.start}")
	public void run() {
		solicitudesDespachoService.run();
		ordenesAlistamientoIntegrationService.run();
		
		//CREAR SUSCRIPCION PARA DETECTAR CREACION DE ORDEN DE ALISTAMIENTO
		//WMS CREA SUSCRIPCION
		//WMS DEBE DETECTAR LAS ORDENES DE ALISTAMIENTO CREADAS (COMO D365 FACTURAS)
		//WMS DEBE NOTIFICAR POR WS A EC QUE LA ORDEN HA SIDO CREADA EN WMS
		//	EC CAMBIAR ESTADO DE MENSAJE A PROCESADO
		//	EC CAMBIAR ESTADO ORDEN A MENSAJE CREADO
		//	WMS DEBE FINALIZAR SUSCRIPCION
		
		//ordenesDeAlistamiento.run();
		//EC DEBE HACER UN PULL DE LAS ORDENES EN ESTADO MENSAJE CREADO
		//EC DEBE HACER UN PUSH PARA CAMBIAR ESTADO DE ORDEN A EN PROCESO
		//EC PUSH PARA CREAR SUSCRIPCION EN WMS PARA DETECTAR QUE LA ORDEN DE ALISTAMIENTO SE ENCUENTRA EN STAGE
		//WMS CREA SUSCRIPCION
		//WMS DEBE DETECTAR LAS ORDENES DE ALISTAMIENTO EN STAGE (COMO D365 FACTURAS)
		//WMS DEBE NOTIFICAR POR WS A EC QUE LA ORDEN ESTA EN STAGE
		//	EC CREA LAS LINEAS DE MERCANCIA DESPACHADA, CANCELADA Y LOS LOTES
		//	EC CAMBIA EL ESTADO DE LA ORDEN A STAGE
		//EC DEBE NOTIFICAR A GWS QUE LA ORDEN QUEDO EN STAge igual que se notificanb las cajas de d365
		//EC CAMBIA SUB ESTADO A NOTIFICADO
		
		//EC DEBE INICIA UN FLUJO PARA DECARGAR LOS DATOS DE LOS DOCUMENTOS DIGITALES
		
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
