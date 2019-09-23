package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.ArchivoExcelInputServiceImpl;
import com.egakat.io.commons.components.MapEntidadManufacturaDecorator;
import com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura;
import com.egakat.io.commons.solicitudes.repository.manufacturas.ManufacturaRepository;
import com.egakat.io.solicitudes.cedis.service.api.ManufacturasInputService;

public abstract class AbstractManufacturasInputServiceImpl extends ArchivoExcelInputServiceImpl<Manufactura> implements ManufacturasInputService{

	private static final String WORKSHEET_NAME = "ORDENES";

	@Autowired
	private ManufacturaRepository repository;

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}

	@Override
	protected ManufacturaRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Manufactura, Long> getMapEntidadDecorator(Decorator<Manufactura, Long> inner) {
		return new MapEntidadManufacturaDecorator(inner);
	}
}