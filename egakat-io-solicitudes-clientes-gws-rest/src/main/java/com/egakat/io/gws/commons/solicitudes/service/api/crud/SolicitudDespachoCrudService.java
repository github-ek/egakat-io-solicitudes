package com.egakat.io.gws.commons.solicitudes.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentDto;

@Transactional(readOnly = true)
public interface SolicitudDespachoCrudService extends CrudService<SolicitudDespachoDto, Long> {

	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	List<SolicitudDespachoDto> findAllByCorrelacionAndEstadoIntegracionIn(String correlacion,
			List<EstadoIntegracionType> estados);

	// TODO esto debe estar en una entidad de ordenes de alistamiento

	@Transactional
	void upload(OrdShipmentDto ord);

}
