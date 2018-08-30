package com.egakat.io.solicitudes.gws.repository.solicitudes;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.solicitudes.SolicitudDespachoLinea;

public interface SolicitudDespachoLineaRepository
		extends IdentifiedDomainObjectRepository<SolicitudDespachoLinea, Long> {
	List<SolicitudDespachoLinea> findAllByIdSolicitudDespacho(Long id);
}
