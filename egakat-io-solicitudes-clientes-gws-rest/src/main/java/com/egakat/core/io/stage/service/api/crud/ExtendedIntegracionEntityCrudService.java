package com.egakat.core.io.stage.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;

public interface ExtendedIntegracionEntityCrudService<M extends IntegrationEntityDto>
		extends IntegracionEntityCrudService<M> {

	@Transactional
	M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado);

	@Transactional
	M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores);

	@Transactional
	M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado);

	@Transactional
	M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores);
}