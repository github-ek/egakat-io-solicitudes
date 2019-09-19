package com.egakat.io.clientes.diageo.service.impl;

import org.springframework.stereotype.Service;

import com.egakat.io.clientes.diageo.dto.IngredionSolicitudDespachoDto;
import com.egakat.io.clientes.diageo.dto.IngredionSolicitudDespachoLineaDto;
import com.egakat.io.clientes.diageo.service.api.IngredionSolicitudDespachoCrudService;
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