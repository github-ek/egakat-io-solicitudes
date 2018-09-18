package com.egakat.io.gws.deprecated.core.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.web.client.service.api.CacheEvictSupported;
import com.egakat.io.gws.commons.core.domain.IntegrationEntity;


public interface DataQualityService<T extends IntegrationEntity> extends CacheEvictSupported{

	@Transactional(readOnly = true)
	List<String> getCorrelacionesPendientes();

	@Transactional
	void transformar(String correlacion);
}
