package com.egakat.io.solicitudes.gws.service.api.crud;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto;

@Transactional(readOnly = true)
public interface SolicitudDespachoCrudService extends CrudService<SolicitudDespachoDto, Long> {

}
