package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.excel.ArchivoExcelInputServiceImpl;
import com.egakat.io.solicitudes.cedis.service.api.AbastecimientosInputService;
import com.egakat.io.solicitudes.components.decorators.MapEntidadTrasladoDecorator;
import com.egakat.io.solicitudes.domain.salidas.Traslado;
import com.egakat.io.solicitudes.repository.salidas.TrasladoRepository;

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