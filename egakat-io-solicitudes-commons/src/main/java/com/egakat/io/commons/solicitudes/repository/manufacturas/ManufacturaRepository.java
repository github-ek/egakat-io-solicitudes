package com.egakat.io.commons.solicitudes.repository.manufacturas;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.integration.commons.archivos.enums.EstadoRegistroType;
import com.egakat.integration.commons.archivos.repository.RegistroRepository;
import com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura;

public interface ManufacturaRepository extends RegistroRepository<Manufactura> {

	@Override
	@Query("SELECT DISTINCT a.idArchivo FROM Manufactura a WHERE a.estado IN (:estados) ORDER BY a.idArchivo")
	List<Long> findAllArchivoIdByEstadoIn(@Param("estados") List<EstadoRegistroType> estado);
}
