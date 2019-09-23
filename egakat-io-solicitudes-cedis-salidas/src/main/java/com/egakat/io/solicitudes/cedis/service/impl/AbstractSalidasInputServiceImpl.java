package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.ArchivoExcelInputServiceImpl;
import com.egakat.io.commons.components.MapEntidadSalidaDecorator;
import com.egakat.io.commons.solicitudes.domain.salidas.Salida;
import com.egakat.io.commons.solicitudes.repository.salidas.SalidaRepository;
import com.egakat.io.solicitudes.cedis.service.api.SalidasInputService;

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