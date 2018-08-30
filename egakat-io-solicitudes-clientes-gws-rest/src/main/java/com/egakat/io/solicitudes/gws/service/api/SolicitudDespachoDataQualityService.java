package com.egakat.io.solicitudes.gws.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.solicitudes.gws.domain.solicitudes.SolicitudDespacho;

@Transactional(readOnly = true)
public interface SolicitudDespachoDataQualityService extends DataQualityService<SolicitudDespacho> {

}
