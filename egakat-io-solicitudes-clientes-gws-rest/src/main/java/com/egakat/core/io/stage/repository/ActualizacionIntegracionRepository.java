package com.egakat.core.io.stage.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import com.egakat.core.io.stage.domain.ActualizacionIntegracion;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionRepository extends IntegrationEntityRepository<ActualizacionIntegracion> {

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion);

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(String integracion,
			EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion);

	Slice<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion, Pageable pageable);

	Slice<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(String integracion,
			EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion, Pageable pageable);

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(String integracion,
			EstadoIntegracionType[] estadosIntegracion, EstadoNotificacionType estadoNotificacion);

	@Query("SELECT DISTINCT a.correlacion FROM ActualizacionIntegracion a WHERE a.estadoIntegracion IN (:estados) ORDER BY a.correlacion")
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);
}