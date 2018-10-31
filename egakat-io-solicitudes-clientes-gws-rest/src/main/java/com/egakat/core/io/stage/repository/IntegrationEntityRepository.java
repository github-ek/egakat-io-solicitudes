package com.egakat.core.io.stage.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.NoRepositoryBean;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.core.io.stage.domain.IntegrationEntity;

@NoRepositoryBean
public interface IntegrationEntityRepository<E extends IntegrationEntity>
		extends IdentifiedDomainObjectRepository<E, Long> {

	boolean existsByIntegracionAndIdExterno(String integracion, String idExterno);

	Stream<E> findAllByIntegracionAndIdExterno(String integracion, String idExterno);
}
