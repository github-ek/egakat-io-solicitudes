package com.egakat.io.gws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoConfirmacionCreacionOrdenesAckPushNotificacionServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoConfirmacionCreacionOrdenesPullServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoConfirmacionOrdenesEnStageAckPushNotificacionServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoConfirmacionOrdenesEnStagePullServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoCreacionSuscripcionPushServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoDownloadServiceImpl;
import com.egakat.io.gws.service.impl.ordenes.OrdenesAlistamientoMapServiceImpl;
import com.egakat.io.gws.service.impl.solicitudes.SolicitudesDespachoNotificacionStagePushServiceImpl;

@Service
public class OrdenesAlistamientoIntegrationService {

	@Autowired
	private OrdenesAlistamientoPullService pullService;

	@Autowired
	private OrdenesAlistamientoCreacionSuscripcionPushServiceImpl creacionSuscripcionPushService;

	@Autowired
	private OrdenesAlistamientoConfirmacionCreacionOrdenesPullServiceImpl confirmacionCreacionOrdenesPullService;

	@Autowired
	private OrdenesAlistamientoConfirmacionCreacionOrdenesAckPushNotificacionServiceImpl confirmacionCreacionOrdenesAckPushService;

	@Autowired
	private OrdenesAlistamientoConfirmacionOrdenesEnStagePullServiceImpl confirmacionOrdenesEnStagePullService;

	@Autowired
	private OrdenesAlistamientoConfirmacionOrdenesEnStageAckPushNotificacionServiceImpl confirmacionOrdenesEnStageAckPushService;

	@Autowired
	private OrdenesAlistamientoDownloadServiceImpl downloadService;

	@Autowired
	private OrdenesAlistamientoMapServiceImpl mapService;
	
	@Autowired
	private SolicitudesDespachoNotificacionStagePushServiceImpl pushService;
	
	
	public void run() {
		pullService.pull();
		creacionSuscripcionPushService.push();
		confirmacionCreacionOrdenesPullService.pull();
		confirmacionCreacionOrdenesAckPushService.push();
		confirmacionOrdenesEnStagePullService.pull();
		confirmacionOrdenesEnStageAckPushService.push();
		downloadService.download();
		mapService.map();
		pushService.push();
	}
}
