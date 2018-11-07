package com.egakat.io.core.service.api.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;

public interface ActualizacionCrudService extends IntegracionEntityCrudService<ActualizacionDto> {

	@Transactional(readOnly = true)
	List<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(String integracion,
			EstadoIntegracionType estadoIntegracion, String... subEstadoIntegracion);

	@Transactional(readOnly = true)
	Slice<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion, Pageable pageable);

	@Transactional(readOnly = true)
	Slice<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(String integracion,
			EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion, Pageable pageable);

	@Transactional(readOnly = true)
	List<ActualizacionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados);

	@Transactional(readOnly = true)
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	@Transactional
	void enqueue(ActualizacionDto model);

	@Transactional
	ActualizacionDto update(ActualizacionDto model, EstadoIntegracionType estado, List<ErrorIntegracionDto> errores);

	@Transactional
	ActualizacionDto updateEstadoNotificacion(ActualizacionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error);

}
