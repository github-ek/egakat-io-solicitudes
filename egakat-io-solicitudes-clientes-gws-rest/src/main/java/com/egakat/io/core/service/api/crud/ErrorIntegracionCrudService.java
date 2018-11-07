package com.egakat.io.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.enums.EstadoNotificacionType;

public interface ErrorIntegracionCrudService extends CrudService<ErrorIntegracionDto, Long> {

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAll(IntegracionEntityDto model);

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAllByEstadoNotificacionIn(String integracion,
			EstadoNotificacionType... estadosNotificacion);

	@Transactional
	void create(String integracion, String correlacion, String codigo, String mensaje, String... arg);

	@Transactional
	void create(String integracion, String correlacion, String codigo, Throwable t);

	@Transactional
	void create(IntegracionEntityDto model, String codigo, String mensaje, String... arg);

	@Transactional
	void create(IntegracionEntityDto model, String codigo, Throwable t);

	ErrorIntegracionDto error(IntegracionEntityDto model, String codigo, String mensaje, String... arg);

	ErrorIntegracionDto error(IntegracionEntityDto model, String codigo, Throwable t);

}
