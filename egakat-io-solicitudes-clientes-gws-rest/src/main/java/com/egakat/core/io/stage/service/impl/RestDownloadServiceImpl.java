package com.egakat.core.io.stage.service.impl;

import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;

abstract public class RestDownloadServiceImpl<I, M extends IntegrationEntityDto> extends DownloadServiceImpl<I, M> {

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	protected String getUrl() {
		return getProperties().getBasePath() + getApiEndPoint();
	}

	abstract protected String getQuery();

}
