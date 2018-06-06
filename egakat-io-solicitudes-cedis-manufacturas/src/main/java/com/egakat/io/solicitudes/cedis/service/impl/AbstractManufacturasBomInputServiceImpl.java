package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.excel.ArchivoExcelInputServiceImpl;
import com.egakat.io.solicitudes.cedis.service.api.ManufacturasBomInputService;
import com.egakat.io.solicitudes.components.decorators.MapEntidadManufacturaBomDecorator;
import com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom;
import com.egakat.io.solicitudes.repository.manufacturas.ManufacturaBomRepository;

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