package com.egakat.io.clientes.ingredion.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.clientes.ingredion.dto.IngredionSolicitudDespachoDto;
import com.egakat.io.clientes.ingredion.dto.IngredionSolicitudDespachoLineaDto;
import com.egakat.io.clientes.ingredion.service.api.IngredionSolicitudDespachoCrudService;
import com.egakat.io.commons.solicitudes.service.impl.AbstractSolicitudDespachoCrudServiceImpl;

@Service
public class IngredionSolicitudDespachoCrudServiceImpl
		extends AbstractSolicitudDespachoCrudServiceImpl<IngredionSolicitudDespachoDto, IngredionSolicitudDespachoLineaDto> implements IngredionSolicitudDespachoCrudService{

	@Override
	protected IngredionSolicitudDespachoDto newModel() {
		return new IngredionSolicitudDespachoDto();
	}

	@Override
	protected IngredionSolicitudDespachoLineaDto newItemModel() {
		return new IngredionSolicitudDespachoLineaDto();
	}
}