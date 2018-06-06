package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.excel.ArchivoExcelInputServiceImpl;
import com.egakat.io.solicitudes.cedis.service.api.SalidasInputService;
import com.egakat.io.solicitudes.components.decorators.MapEntidadSalidaDecorator;
import com.egakat.io.solicitudes.domain.salidas.Salida;
import com.egakat.io.solicitudes.repository.salidas.SalidaRepository;

public abstract class AbstractSalidasInputServiceImpl extends ArchivoExcelInputServiceImpl<Salida> implements SalidasInputService{

	private static final String WORKSHEET_NAME = "SOLICITUDES";

	@Autowired
	private SalidaRepository repository;

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}

	@Override
	protected SalidaRepository getRepository() {
		return repository;
	}


	@Override
	protected Decorator<Salida, Long> getMapEntidadDecorator(Decorator<Salida, Long> inner) {
		return new MapEntidadSalidaDecorator(inner);
	}
}