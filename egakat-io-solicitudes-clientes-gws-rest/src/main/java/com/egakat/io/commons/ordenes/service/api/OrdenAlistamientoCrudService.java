package com.egakat.io.commons.ordenes.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;

public interface OrdenAlistamientoCrudService
		extends ExtendedIntegracionEntityCrudService<OrdenAlistamientoDto> {

	//TODO ELIMINAR
	@Transactional(readOnly = false)
	Long upload(OrdenAlistamientoDto model, List<ErrorIntegracionDto> errores);

	//TODO ELIMINAR
	@Transactional
	void updateEstadoNoficacionOrdenAlistamiento(OrdenAlistamientoDto model, ActualizacionDto actualizacion, EstadoIntegracionType estado, Long id);
}
