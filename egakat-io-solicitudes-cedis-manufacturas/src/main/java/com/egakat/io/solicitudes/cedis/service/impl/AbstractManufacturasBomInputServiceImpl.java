package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.ArchivoExcelInputServiceImpl;
import com.egakat.io.commons.components.MapEntidadManufacturaBomDecorator;
import com.egakat.io.commons.solicitudes.domain.manufacturas.ManufacturaBom;
import com.egakat.io.commons.solicitudes.repository.manufacturas.ManufacturaBomRepository;
import com.egakat.io.solicitudes.cedis.service.api.ManufacturasBomInputService;

public abstract class AbstractManufacturasBomInputServiceImpl extends ArchivoExcelInputServiceImpl<ManufacturaBom> implements ManufacturasBomInputService{

	private static final String WORKSHEET_NAME = "SOLICITUDES";

	@Autowired
	private ManufacturaBomRepository repository;

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}

	@Override
	protected ManufacturaBomRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<ManufacturaBom, Long> getMapEntidadDecorator(Decorator<ManufacturaBom, Long> inner) {
		return new MapEntidadManufacturaBomDecorator(inner);
	}
}