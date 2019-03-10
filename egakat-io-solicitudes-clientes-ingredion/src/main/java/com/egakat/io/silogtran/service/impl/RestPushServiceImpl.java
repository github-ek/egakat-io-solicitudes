package com.egakat.io.silogtran.service.impl;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.ingredion.dto.Reintentable;

import lombok.val;

abstract public class RestPushServiceImpl<ID, I extends Reintentable<ID>, O, R> extends PushServiceImpl<ID, I, O, R> {

	protected abstract RestProperties getProperties();

	protected abstract RestClient getRestClient();

	protected abstract String getApiEndPoint();

	protected String getUrl() {
		val result = getProperties().getBasePath() + getApiEndPoint();
		return result;
	}
}