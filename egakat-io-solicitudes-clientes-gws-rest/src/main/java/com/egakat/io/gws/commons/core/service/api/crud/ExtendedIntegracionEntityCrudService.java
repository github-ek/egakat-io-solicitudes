package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;

public interface ExtendedIntegracionEntityCrudService<M extends IntegrationEntityDto, S>
		extends IntegracionEntityCrudService<M> {

	@Transactional(readOnly = true)
	List<M> findTopByEstado(S estado);

	@Transactional(readOnly = true)
	Slice<M> findByEstado(S estado, Pageable pageable);

	@Transactional
	M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado);

	@Transactional
	M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado);

}