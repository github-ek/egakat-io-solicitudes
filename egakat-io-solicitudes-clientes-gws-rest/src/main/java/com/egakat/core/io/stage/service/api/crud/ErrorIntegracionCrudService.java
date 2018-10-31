package com.egakat.core.io.stage.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;
import com.egakat.core.services.crud.api.CrudService;

public interface ErrorIntegracionCrudService extends CrudService<ErrorIntegracionDto, Long> {

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAll(IntegrationEntityDto model);

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAllByEstadoNotificacionIn(String integracion,
			EstadoNotificacionType... estadosNotificacion);

	@Transactional
	void create(String integracion, String correlacion, String codigo, String mensaje, String... arg);

	@Transactional
	void create(String integracion, String correlacion, String codigo, Throwable t);

	@Transactional
	void create(IntegrationEntityDto model, String codigo, String mensaje, String... arg);

	@Transactional
	void create(IntegrationEntityDto model, String codigo, Throwable t);

	ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, String mensaje, String... arg);

	ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, Throwable t);

}
