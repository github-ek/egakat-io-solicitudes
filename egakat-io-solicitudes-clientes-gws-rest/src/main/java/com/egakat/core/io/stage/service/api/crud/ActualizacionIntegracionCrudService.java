package com.egakat.core.io.stage.service.api.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionCrudService extends IntegracionEntityCrudService<ActualizacionIntegracionDto> {

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(
			String integracion, EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion);

	@Transactional(readOnly = true)
	Slice<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion, Pageable pageable);

	@Transactional(readOnly = true)
	Slice<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(String integracion,
			EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion, Pageable pageable);


	@Transactional(readOnly = true)
	List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	
	@Transactional
	void enqueue(ActualizacionIntegracionDto model);

	@Transactional
	ActualizacionIntegracionDto update(ActualizacionIntegracionDto model, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores);

	@Transactional
	ActualizacionIntegracionDto updateEstadoNotificacion(ActualizacionIntegracionDto model,
			List<ErrorIntegracionDto> errores, EstadoNotificacionType ok, EstadoNotificacionType error);

}
