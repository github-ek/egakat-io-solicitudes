package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasInputService;

@Service(ManufacturasInputService.ESTAMPILLADOS_SERVICE)
public class EstampilladosInputServiceImpl extends AbstractManufacturasInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_ESTAMPILLADOS";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}