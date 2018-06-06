package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasBomInputService;

@Service(ManufacturasBomInputService.OFERTAS_BOM_SERVICE)
public class OfertasBomInputServiceImpl extends AbstractManufacturasBomInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_OFERTAS_BOM";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}