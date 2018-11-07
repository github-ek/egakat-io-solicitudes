package com.egakat.io.core.repository;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.core.domain.ErrorIntegracion;
import com.egakat.io.core.enums.EstadoNotificacionType;

public interface ErrorIntegracionRepository extends IdentifiedDomainObjectRepository<ErrorIntegracion, Long> {

	List<ErrorIntegracion> findAllByIntegracionAndEstadoNotificacionIn(String integracion,
			EstadoNotificacionType[] estados);

	List<ErrorIntegracion> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion, String idExterno,
			String correlacion);
}