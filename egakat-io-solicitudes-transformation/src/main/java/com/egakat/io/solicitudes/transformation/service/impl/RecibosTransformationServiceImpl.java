package com.egakat.io.solicitudes.transformation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.domain.recibos.Recibo;
import com.egakat.io.solicitudes.repository.recibos.ReciboRepository;
import com.egakat.io.solicitudes.transformation.service.api.RecibosTransformationService;

@Service
public class RecibosTransformationServiceImpl extends SolicitudesTerceroTransformationImpl<Recibo>
		implements RecibosTransformationService {

	@Autowired
	private ReciboRepository repository;

	@Override
	protected ReciboRepository getRepository() {
		return repository;
	}
}