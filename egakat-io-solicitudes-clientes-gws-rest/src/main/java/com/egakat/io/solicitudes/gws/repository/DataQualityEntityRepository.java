package com.egakat.io.solicitudes.gws.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.solicitudes.gws.domain.DataQualityEntity;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

@NoRepositoryBean
public interface DataQualityEntityRepository<T extends DataQualityEntity> extends IdentifiedDomainObjectRepository<T, Long> {
	List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados);

	List<T> findAllByCorrelacionAndEstadoIntegracionIn(String correlacion, List<EstadoIntegracionType> estados);

	List<T> findAllByCorrelacionAndEstadoIntegracionNotIn(String correlacion, List<EstadoIntegracionType> estados);
}
