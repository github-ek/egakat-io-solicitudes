package com.egakat.io.gws.commons.ordenes.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoDto;

public interface OrdenAlistamientoCrudService
		extends ExtendedIntegracionEntityCrudService<OrdenAlistamientoDto> {

	//TODO ELIMINAR
	@Transactional(readOnly = false)
	Long upload(OrdenAlistamientoDto model, List<ErrorIntegracionDto> errores);

	//TODO ELIMINAR
	@Transactional
	void updateEstadoNoficacionOrdenAlistamiento(OrdenAlistamientoDto model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado, Long id);
}
