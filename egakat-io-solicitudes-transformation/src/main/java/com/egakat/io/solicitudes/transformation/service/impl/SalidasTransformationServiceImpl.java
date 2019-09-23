package com.egakat.io.solicitudes.transformation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.commons.solicitudes.domain.salidas.Salida;
import com.egakat.io.commons.solicitudes.repository.salidas.SalidaRepository;
import com.egakat.io.solicitudes.transformation.service.api.SalidasTransformationService;

@Service
public class SalidasTransformationServiceImpl extends SolicitudesTerceroTransformationImpl<Salida>
		implements SalidasTransformationService {

	@Autowired
	private SalidaRepository repository;

	@Override
	protected SalidaRepository getRepository() {
		return repository;
	}
}