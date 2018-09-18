package com.egakat.io.gws.commons.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.io.gws.commons.core.domain.ActualizacionIntegracion;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionRepository extends IntegrationEntityRepository<ActualizacionIntegracion> {

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion);

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(String integracion,
			EstadoIntegracionType[] estadosIntegracion, EstadoNotificacionType estadoNotificacion);

	@Query("SELECT DISTINCT a.correlacion FROM ActualizacionIntegracion a WHERE a.estadoIntegracion IN (:estados) ORDER BY a.correlacion")
	List<String> findAllCorrelacionesByEstadoIntegracionIn(@Param("estados") List<EstadoIntegracionType> estados);

	// Optional<ActualizacionIntegracion>
	// findByIntegracionAndCorrelacionAndIdExterno(String integracion, String
	// correlacion, String idExterno);

}