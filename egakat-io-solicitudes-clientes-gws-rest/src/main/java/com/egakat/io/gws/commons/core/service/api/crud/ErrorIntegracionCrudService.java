package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;

public interface ErrorIntegracionCrudService extends CrudService<ErrorIntegracionDto, Long> {

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion, String idExterno,
			String correlacion);

	@Transactional(readOnly = true)
	List<ErrorIntegracionDto> findAllByEstadoNotificacionIn(String integracion,
			EstadoNotificacionType... estadosNotificacion);

	@Transactional
	void create(String integracion, String correlacion, String idExterno, String codigo, String mensaje, String... arg);

	@Transactional
	void create(IntegrationEntityDto model, String codigo, String mensaje, String... arg);

	@Transactional
	void create(String integracion, String correlacion, String idExterno, String codigo, Throwable t);

	@Transactional
	void create(IntegrationEntityDto model, String codigo, Throwable t);

	ErrorIntegracionDto error(String integracion, String correlacion, String idExterno, String codigo, String mensaje,
			String... arg);

	ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, String mensaje, String... arg);

	ErrorIntegracionDto error(String integracion, String correlacion, String idExterno, String codigo, Throwable t);

	ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, Throwable t);
}
