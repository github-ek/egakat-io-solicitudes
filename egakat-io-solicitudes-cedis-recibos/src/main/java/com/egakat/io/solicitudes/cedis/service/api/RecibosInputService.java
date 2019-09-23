package com.egakat.io.solicitudes.cedis.service.api;

import com.egakat.integration.core.files.service.api.InputService;
import com.egakat.io.commons.solicitudes.domain.recibos.Recibo;

public interface RecibosInputService extends InputService<Recibo> {

	final String COMPRAS_EXTRACT_SERVICE = "comprasExtractService";

	final String DEVOLUCIONES_EXTRACT_SERVICE = "devolucionesExtractService";

}
