package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.ManufacturasBomInputService;

@Service(ManufacturasBomInputService.ACONDICIONAMIENTOS_SERVICE)
public class AcondicionamientoInputServiceImpl extends AbstractManufacturasBomInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_ACONDICIONAMIENTO";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}