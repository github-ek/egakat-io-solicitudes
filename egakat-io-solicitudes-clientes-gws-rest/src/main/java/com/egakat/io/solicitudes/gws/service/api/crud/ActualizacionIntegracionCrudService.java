package com.egakat.io.solicitudes.gws.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionCrudService extends CrudService<ActualizacionIntegracionDto, Long> {

	@Transactional(readOnly = true)
	ActualizacionIntegracionDto findOneByIntegracionAndCorrelacionAndIdExterno(String integracion, String correlacion,
			String idExterno);

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadosIntegracion);

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadoIntegracion);

	@Transactional
	void enqueue(ActualizacionIntegracionDto model);

	@Transactional
	void updateEstadoIntegracion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoIntegracionType ok, EstadoIntegracionType error);

	@Transactional
	void updateEstadoNotificacion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error);

}
