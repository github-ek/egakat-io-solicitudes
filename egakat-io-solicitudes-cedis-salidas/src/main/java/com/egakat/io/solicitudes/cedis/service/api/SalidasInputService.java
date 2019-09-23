package com.egakat.io.solicitudes.cedis.service.api;

import com.egakat.integration.core.files.service.api.InputService;
import com.egakat.io.commons.solicitudes.domain.salidas.Salida;

public interface SalidasInputService extends InputService<Salida> {

	final String SALIDAS_SERVICE = "salidasService";
	
	final String XD_SERVICE = "crossDockingService";

}
