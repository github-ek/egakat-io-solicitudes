package com.egakat.io.solicitudes.gws.repository;

import java.util.List;
import java.util.Optional;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.ActualizacionIntegracion;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.enums.EstadoNotificacionType;

public interface ActualizacionIntegracionRepository
		extends IdentifiedDomainObjectRepository<ActualizacionIntegracion, Long> {

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estadosIntegracion);

	List<ActualizacionIntegracion> findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(String integracion,
			EstadoIntegracionType[] estadosIntegracion,
			EstadoNotificacionType estadoNotificacion);

	Optional<ActualizacionIntegracion> findByIntegracionAndIdExterno(String integracion, String idExterno);
}
