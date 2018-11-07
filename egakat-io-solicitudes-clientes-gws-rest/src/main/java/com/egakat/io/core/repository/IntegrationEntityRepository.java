package com.egakat.io.core.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.NoRepositoryBean;

import com.egakat.core.data.jpa.repository.IdentifiedDomainObjectRepository;
import com.egakat.io.core.domain.IntegracionEntity;

@NoRepositoryBean
public interface IntegrationEntityRepository<E extends IntegracionEntity>
		extends IdentifiedDomainObjectRepository<E, Long> {

	boolean existsByIntegracionAndIdExterno(String integracion, String idExterno);

	Stream<E> findAllByIntegracionAndIdExterno(String integracion, String idExterno);
}
