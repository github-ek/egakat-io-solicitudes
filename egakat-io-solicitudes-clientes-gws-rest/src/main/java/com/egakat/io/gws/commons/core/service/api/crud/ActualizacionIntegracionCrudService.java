package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionCrudService extends IntegracionEntityCrudService<ActualizacionIntegracionDto> {

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	@Transactional
	void enqueue(ActualizacionIntegracionDto model);

	@Transactional
	void updateEstadoIntegracion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoIntegracionType ok, EstadoIntegracionType error);

	@Transactional
	void updateEstadoNotificacion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error);
}
