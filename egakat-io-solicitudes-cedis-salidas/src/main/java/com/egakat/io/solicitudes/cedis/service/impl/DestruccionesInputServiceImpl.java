package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.excel.ArchivoExcelInputServiceImpl;
import com.egakat.io.solicitudes.cedis.service.api.DestruccionesInputService;
import com.egakat.io.solicitudes.components.decorators.MapEntidadDestruccionDecorator;
import com.egakat.io.solicitudes.domain.salidas.Destruccion;
import com.egakat.io.solicitudes.repository.salidas.DestruccionRepository;

@Service(DestruccionesInputService.DESTRUCCIONES_SERVICE)
public class DestruccionesInputServiceImpl extends ArchivoExcelInputServiceImpl<Destruccion> implements DestruccionesInputService{

	private static final String TIPO_ARCHIVO_CODIGO = "PLANTILLA_DESTRUCCIONES";

	private static final String WORKSHEET_NAME = "SOLICITUDES";

	@Autowired
	private DestruccionRepository repository;

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}
	
	@Override
	protected DestruccionRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Destruccion,Long> getMapEntidadDecorator(Decorator<Destruccion,Long> inner) {
		return new MapEntidadDestruccionDecorator(inner);
	}
}
