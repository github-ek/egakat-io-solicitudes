package com.egakat.io.solicitudes.transformation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.commons.solicitudes.domain.salidas.Destruccion;
import com.egakat.io.commons.solicitudes.repository.salidas.DestruccionRepository;
import com.egakat.io.solicitudes.transformation.service.api.DestruccionesTransformationService;

@Service
public class DestruccionesTransformationServiceImpl extends SolicitudesTransformationServiceImpl<Destruccion>
		implements DestruccionesTransformationService {

	@Autowired
	private DestruccionRepository repository;

	@Override
	protected DestruccionRepository getRepository() {
		return repository;
	}
}