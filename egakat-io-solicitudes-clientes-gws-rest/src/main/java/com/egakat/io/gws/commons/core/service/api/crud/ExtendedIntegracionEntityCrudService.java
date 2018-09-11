package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;


public interface ExtendedIntegracionEntityCrudService<M extends IntegrationEntityDto, S>
		extends IntegracionEntityCrudService<M> {

	@Transactional
	M download(M model, ActualizacionIntegracionDto actualizacion);

	@Transactional(readOnly = true)
	List<M> findTopByEstado(S estado);

	@Transactional(readOnly = true)
	Slice<M> findByEstado(S estado, Pageable pageable);
}