package com.egakat.io.core.service.impl.rest;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.core.service.api.PullService;
import com.egakat.io.core.service.impl.PullServiceImpl;

import lombok.val;

abstract public class RestPullServiceImpl<I> extends PullServiceImpl<I> implements PullService {

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	protected String getUrl() {
		val result = getProperties().getBasePath() + getApiEndPoint();
		return result;
	}

	abstract protected String getQuery();
}