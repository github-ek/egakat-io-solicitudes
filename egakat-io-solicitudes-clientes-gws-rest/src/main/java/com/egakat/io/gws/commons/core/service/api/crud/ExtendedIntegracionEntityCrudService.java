package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;

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