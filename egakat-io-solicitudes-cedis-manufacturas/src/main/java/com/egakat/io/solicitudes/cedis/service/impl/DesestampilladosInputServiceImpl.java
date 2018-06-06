package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasInputService;

@Service(ManufacturasInputService.DESESTAMPILLADOS_SERVICE)
public class DesestampilladosInputServiceImpl extends AbstractManufacturasInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_DESESTAMPILLADOS";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}