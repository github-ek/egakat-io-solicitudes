package com.egakat.core.io.stage.repository;

import java.util.List;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.core.io.stage.domain.ErrorIntegracion;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;

public interface ErrorIntegracionRepository extends IdentifiedDomainObjectRepository<ErrorIntegracion, Long> {

	List<ErrorIntegracion> findAllByIntegracionAndEstadoNotificacionIn(String integracion,
			EstadoNotificacionType[] estados);

	List<ErrorIntegracion> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion, String idExterno,
			String correlacion);
}