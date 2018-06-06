package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.SalidasInputService;

@Service(SalidasInputService.XD_SERVICE)
public class CrossDockingInputServiceImpl extends AbstractSalidasInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_XD";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}