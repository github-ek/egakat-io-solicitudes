package com.egakat.io.gws.commons.ordenes.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoDto;

public interface OrdenAlistamientoCrudService
		extends ExtendedIntegracionEntityCrudService<OrdenAlistamientoDto, String> {

	//TODO ELIMINAR
	@Transactional(readOnly = false)
	Long upload(OrdenAlistamientoDto model, List<ErrorIntegracionDto> errores);

	//TODO ELIMINAR
	@Transactional
	void updateEstadoNoficacionOrdenAlistamiento(OrdenAlistamientoDto model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado, Long id);
}
