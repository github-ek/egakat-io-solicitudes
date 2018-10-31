package com.egakat.core.io.stage.service.impl;

import com.egakat.core.io.stage.service.api.PullService;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;

abstract public class RestPullServiceImpl<I> extends PullServiceImpl<I> implements PullService {

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	protected String getUrl() {
		return getProperties().getBasePath() + getApiEndPoint();
	}

	abstract protected String getQuery();
}