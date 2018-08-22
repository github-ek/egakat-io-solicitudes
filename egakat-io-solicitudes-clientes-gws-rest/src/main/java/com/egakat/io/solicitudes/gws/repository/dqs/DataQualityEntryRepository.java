package com.egakat.io.solicitudes.gws.repository.dqs;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.dqs.DataQualityEntry;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

@NoRepositoryBean
public interface DataQualityEntryRepository<T extends DataQualityEntry> extends IdentifiedDomainObjectRepository<T, Long> {
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	List<T> findAllByCorrelacionAndEstadoIntegracionIn(String correlacion, List<EstadoIntegracionType> estados);

	List<T> findAllByCorrelacionAndEstadoIntegracionNotIn(String correlacion, List<EstadoIntegracionType> estados);
}
