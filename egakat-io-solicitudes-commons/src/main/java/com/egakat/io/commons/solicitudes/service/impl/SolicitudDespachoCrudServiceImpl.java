package com.egakat.io.commons.solicitudes.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;

@Service
public class SolicitudDespachoCrudServiceImpl
		extends AbstractSolicitudDespachoCrudServiceImpl<SolicitudDespachoDto, SolicitudDespachoLineaDto> implements SolicitudDespachoCrudService{

	@Override
	protected SolicitudDespachoDto newModel() {
		return new SolicitudDespachoDto();
	}

	@Override
	protected SolicitudDespachoLineaDto newItemModel() {
		return new SolicitudDespachoLineaDto();
	}
}