package com.egakat.io.gws.commons.core.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.web.client.service.api.CacheEvictSupported;

public interface PushService extends CacheEvictSupported{

	@Transactional(readOnly = true)
	List<String> getCorrelacionesPendientes();

	void push(String correlacion);

	void push();
}