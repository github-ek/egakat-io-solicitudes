package com.egakat.io.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.enums.EstadoIntegracionType;

public interface ExtendedIntegracionEntityCrudService<M extends IntegracionEntityDto>
		extends IntegracionEntityCrudService<M> {

	@Transactional
	M create(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado);

	@Transactional
	M create(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores);

	@Transactional
	M update(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado);

	@Transactional
	M update(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores);
}