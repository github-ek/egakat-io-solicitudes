package com.egakat.io.ingredion.service.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.ingredion.dto.ActaDto;
import com.egakat.io.ingredion.dto.ErrorDto;
import com.egakat.io.silogtran.dto.RemesaDto;

public interface ActasIngredionAlistadasService {

	@Transactional(readOnly = true)
	List<String> getBodegasAlternas();

	@Transactional(readOnly = true)
	List<ActaDto> getActasAlistadas(LocalDate fechaDesde, LocalDate fechaHasta, List<String> estados,
			List<String> bodegas);

	@Transactional(readOnly = false)
	void marcarActasProcesadas(List<Long> id);
	
	@Transactional(readOnly = false)
	void marcarActasEnvidas(RemesaDto model);

	@Transactional(readOnly = false)
	void errorDuranteEnvio(RemesaDto model, List<ErrorDto> errores);
}
