package com.egakat.io.gws.commons.solicitudes.repository;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespachoLinea;

public interface SolicitudDespachoLineaRepository
		extends IdentifiedDomainObjectRepository<SolicitudDespachoLinea, Long> {
	List<SolicitudDespachoLinea> findAllByIdSolicitudDespacho(Long id);
}
