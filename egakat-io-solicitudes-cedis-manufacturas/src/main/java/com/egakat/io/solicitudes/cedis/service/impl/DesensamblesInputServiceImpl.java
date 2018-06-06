package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasInputService;

@Service(ManufacturasInputService.DESENSAMBLES_SERVICE)
public class DesensamblesInputServiceImpl extends AbstractManufacturasInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_DESENSAMBLES";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}