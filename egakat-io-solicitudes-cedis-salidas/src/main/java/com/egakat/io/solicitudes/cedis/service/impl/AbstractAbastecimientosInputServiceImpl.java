package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.ArchivoExcelInputServiceImpl;
import com.egakat.io.commons.components.MapEntidadTrasladoDecorator;
import com.egakat.io.commons.solicitudes.domain.salidas.Traslado;
import com.egakat.io.commons.solicitudes.repository.salidas.TrasladoRepository;
import com.egakat.io.solicitudes.cedis.service.api.AbastecimientosInputService;

public abstract class AbstractAbastecimientosInputServiceImpl extends ArchivoExcelInputServiceImpl<Traslado> implements AbastecimientosInputService{
	
	private static final String WORKSHEET_NAME = "SOLICITUDES";

	@Autowired
	private TrasladoRepository repository;

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}

	@Override
	protected TrasladoRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Traslado, Long> getMapEntidadDecorator(Decorator<Traslado, Long> inner) {
		return new MapEntidadTrasladoDecorator(inner);
	}
}