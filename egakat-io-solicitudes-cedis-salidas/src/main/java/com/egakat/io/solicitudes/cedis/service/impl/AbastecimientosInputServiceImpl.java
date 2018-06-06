package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.AbastecimientosInputService;

@Service(AbastecimientosInputService.ABASTECIMIENTOS_SERVICE)
public class AbastecimientosInputServiceImpl extends AbstractAbastecimientosInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_ABASTECIMIENTOS";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}