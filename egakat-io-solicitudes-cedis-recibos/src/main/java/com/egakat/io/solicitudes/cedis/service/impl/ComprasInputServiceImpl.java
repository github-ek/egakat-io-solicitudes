package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.RecibosInputService;

@Service(RecibosInputService.COMPRAS_EXTRACT_SERVICE)
public class ComprasInputServiceImpl extends AbstractRecibosInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_COMPRAS";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}