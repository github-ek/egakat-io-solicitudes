package com.egakat.io.core.service.impl.rest;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.service.impl.PushServiceImpl;

import lombok.val;

abstract public class RestPushServiceImpl<M extends IntegracionEntityDto, O, R> extends PushServiceImpl<M, O, R> {

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	protected String getUrl() {
		val result = getProperties().getBasePath() + getApiEndPoint();
		return result;
	}
}