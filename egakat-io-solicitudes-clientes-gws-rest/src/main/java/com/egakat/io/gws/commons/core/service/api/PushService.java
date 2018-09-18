package com.egakat.io.gws.commons.core.service.api;

import com.egakat.core.web.client.service.api.CacheEvictSupported;

public interface PushService extends CacheEvictSupported{
	void push(String correlacion);

	void push();
}