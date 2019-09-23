package com.egakat.io.commons.solicitudes.repository.recibos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.integration.commons.archivos.enums.EstadoRegistroType;
import com.egakat.integration.commons.archivos.repository.RegistroRepository;
import com.egakat.io.commons.solicitudes.domain.recibos.Recibo;

public interface ReciboRepository extends RegistroRepository<Recibo> {

	@Override
	@Query("SELECT DISTINCT a.idArchivo FROM Recibo a WHERE a.estado IN (:estados) ORDER BY a.idArchivo")
	List<Long> findAllArchivoIdByEstadoIn(@Param("estados") List<EstadoRegistroType> estado);
}
