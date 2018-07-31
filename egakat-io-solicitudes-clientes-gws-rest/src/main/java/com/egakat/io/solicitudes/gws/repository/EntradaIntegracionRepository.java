package com.egakat.io.solicitudes.gws.repository;

import java.util.List;
import java.util.Optional;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.EntradaIntegracion;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;

public interface EntradaIntegracionRepository extends IdentifiedDomainObjectRepository<EntradaIntegracion, Long> {

	List<EntradaIntegracion> findAllByIntegracionAndEstadoIn(String integracion,
			EstadoEntradaIntegracionType[] estados);

	List<EntradaIntegracion> findAllByIntegracionAndProgramarNotificacionAndEstadoIn(String integracion,
			boolean programarNotificacion, EstadoEntradaIntegracionType[] estados);

	Optional<EntradaIntegracion> findByIntegracionAndIdExterno(String integracion, String idExterno);
}
