package com.egakat.io.solicitudes.gws.repository;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.ErrorIntegracion;

public interface ErrorIntegracionRepository extends IdentifiedDomainObjectRepository<ErrorIntegracion, Long> {
	
	List<ErrorIntegracion> findAllByIntegracionAndIdExterno(String integracion, String idExterno);

}
