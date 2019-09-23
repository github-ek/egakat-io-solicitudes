package com.egakat.io.solicitudes.cedis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.service.impl.ArchivoExcelInputServiceImpl;
import com.egakat.io.commons.components.MapEntidadReciboDecorator;
import com.egakat.io.commons.solicitudes.domain.recibos.Recibo;
import com.egakat.io.commons.solicitudes.repository.recibos.ReciboRepository;
import com.egakat.io.solicitudes.cedis.service.api.RecibosInputService;

public abstract class AbstractRecibosInputServiceImpl extends ArchivoExcelInputServiceImpl<Recibo> implements RecibosInputService{

	private static final String WORKSHEET_NAME = "ORDENES";

	@Autowired
	private ReciboRepository repository;

	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}

	@Override
	protected ReciboRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Recibo, Long> getMapEntidadDecorator(Decorator<Recibo, Long> inner) {
		return new MapEntidadReciboDecorator(inner);
	}
}