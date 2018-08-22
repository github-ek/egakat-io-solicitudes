package com.egakat.io.solicitudes.gws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.io.solicitudes.gws.domain.SolicitudDespacho;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.repository.dqs.DataQualityEntryRepository;

public interface SolicitudDespachoRepository extends DataQualityEntryRepository<SolicitudDespacho> {

	@Override
	@Query("SELECT DISTINCT a.correlacion FROM SolicitudDespacho a WHERE a.estadoIntegracion IN (:estados) ORDER BY a.correlacion")
	List<String> findAllCorrelacionesByEstadoIntegracionIn(@Param("estados") List<EstadoIntegracionType> estados);
}
