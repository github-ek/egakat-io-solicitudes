package com.egakat.io.gws.commons.solicitudes.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;

@Transactional(readOnly = true)
public interface SolicitudDespachoLineaCrudService extends CrudService<SolicitudDespachoLineaDto, Long> {

	List<SolicitudDespachoLineaDto> findAllByIdSolicitudDespacho(Long id);

}
