package com.egakat.io.solicitudes.cedis.service.api;

import com.egakat.integration.core.files.service.api.InputService;
import com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura;

public interface ManufacturasInputService extends InputService<Manufactura> {

	final String OFERTAS_SERVICE = "ofertasService";
	
	final String DESENSAMBLES_SERVICE = "desensamblesService";
	
	final String ETIQUETADOS_SERVICE = "etiquetadosService";

	final String ESTAMPILLADOS_SERVICE = "estampilladosService";
	
	final String DESESTAMPILLADOS_SERVICE = "desestampilladosService";
	
}
