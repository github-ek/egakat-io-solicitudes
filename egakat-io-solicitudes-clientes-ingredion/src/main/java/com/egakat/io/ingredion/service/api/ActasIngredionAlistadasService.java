package com.egakat.io.ingredion.service.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.ingredion.dto.ActaDto;

public interface ActasIngredionAlistadasService {

	@Transactional(readOnly = true)
	List<ActaDto> getActasAlistadas(LocalDate fechaDesde, LocalDate fechaHasta, List<String> estados,
			List<Long> bodegas, List<Long> origenes);

	@Transactional(readOnly = false)
	void marcarActasProcesadas(List<Long> id);
}
