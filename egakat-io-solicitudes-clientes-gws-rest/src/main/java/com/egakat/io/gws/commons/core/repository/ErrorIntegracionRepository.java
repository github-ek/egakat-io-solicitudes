package com.egakat.io.gws.commons.core.repository;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.gws.commons.core.domain.ErrorIntegracion;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;

public interface ErrorIntegracionRepository extends IdentifiedDomainObjectRepository<ErrorIntegracion, Long> {

	List<ErrorIntegracion> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion, String idExterno,
			String correlacion);

	List<ErrorIntegracion> findAllByIntegracionAndEstadoNotificacionIn(String integracion,
			EstadoNotificacionType[] estados);
}