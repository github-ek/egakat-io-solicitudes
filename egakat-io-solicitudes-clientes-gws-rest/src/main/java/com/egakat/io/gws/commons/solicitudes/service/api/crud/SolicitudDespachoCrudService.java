package com.egakat.io.gws.commons.solicitudes.service.api.crud;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentDto;


public interface SolicitudDespachoCrudService
		extends ExtendedIntegracionEntityCrudService<SolicitudDespachoDto, String> {

	// TODO esto debe estar en una entidad de ordenes de alistamiento
	@Transactional
	void upload(OrdShipmentDto ord);
}
