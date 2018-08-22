package com.egakat.io.solicitudes.gws.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

@Transactional(readOnly = true)
public interface SolicitudDespachoCrudService extends CrudService<SolicitudDespachoDto, Long> {

	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);
	
	List<SolicitudDespachoDto> findAllByCorrelacionAndEstadoIntegracionIn(String correlacion, List<EstadoIntegracionType> estados);

}
