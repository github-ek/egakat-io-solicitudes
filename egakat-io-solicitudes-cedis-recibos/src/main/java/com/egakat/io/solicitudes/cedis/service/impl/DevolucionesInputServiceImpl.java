package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.cedis.service.api.RecibosInputService;

@Service(RecibosInputService.DEVOLUCIONES_EXTRACT_SERVICE)
public class DevolucionesInputServiceImpl extends AbstractRecibosInputServiceImpl {

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_DEVOLUCIONES";

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
}