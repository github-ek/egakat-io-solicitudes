package com.egakat.io.commons.solicitudes.repository.salidas;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.integration.commons.archivos.enums.EstadoRegistroType;
import com.egakat.integration.commons.archivos.repository.RegistroRepository;
import com.egakat.io.commons.solicitudes.domain.salidas.Salida;

public interface SalidaRepository extends RegistroRepository<Salida> {

	@Override
	@Query("SELECT DISTINCT a.idArchivo FROM Salida a WHERE a.estado IN (:estados) ORDER BY a.idArchivo")
	List<Long> findAllArchivoIdByEstadoIn(@Param("estados") List<EstadoRegistroType> estado);
}
