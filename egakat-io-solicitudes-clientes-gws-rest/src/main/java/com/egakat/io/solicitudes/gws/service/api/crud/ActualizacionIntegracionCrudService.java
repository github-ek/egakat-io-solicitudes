package com.egakat.io.solicitudes.gws.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.enums.EstadoNotificacionType;

@Transactional(readOnly = true)
public interface ActualizacionIntegracionCrudService extends CrudService<ActualizacionIntegracionDto, Long> {

	ActualizacionIntegracionDto findOneAllByEstadoIntegracionIn(String integracion, String idExterno);

	List<ActualizacionIntegracionDto> findAllByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadosIntegracion);

	List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadoIntegracion);

	@Transactional(readOnly = false)
	void enqueue(ActualizacionIntegracionDto model);

	@Transactional(readOnly = false)
	void update(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores, EstadoIntegracionType ok,
			EstadoIntegracionType error);

	@Transactional(readOnly = false)
	void update(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores, EstadoNotificacionType ok,
			EstadoNotificacionType error);

}
