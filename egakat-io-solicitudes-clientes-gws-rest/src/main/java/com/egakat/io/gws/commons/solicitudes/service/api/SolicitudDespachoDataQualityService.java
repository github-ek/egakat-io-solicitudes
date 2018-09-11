package com.egakat.io.gws.commons.solicitudes.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.service.api.deprecated.DataQualityService;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespacho;

@Transactional(readOnly = true)
public interface SolicitudDespachoDataQualityService extends DataQualityService<SolicitudDespacho> {

}
