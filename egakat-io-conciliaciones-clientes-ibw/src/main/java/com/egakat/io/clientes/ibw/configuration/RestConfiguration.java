package com.egakat.io.clientes.ibw.configuration;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.egakat.core.web.client.components.ReintentableResponseErrorHandler;
import com.egakat.core.web.client.configuration.RestTemplateConfiguration;

import lombok.Getter;
import lombok.val;

@Configuration
@Getter
public class RestConfiguration extends RestTemplateConfiguration {

	@Autowired
	private ReintentableResponseErrorHandler errorHandler;

	@Override
	protected HttpClientBuilder getHttpClientBuilder() {
		return super.getHttpClientBuilder().disableCookieManagement();
	}

	@Override
	public RestTemplate restTemplate(RestTemplateBuilder builder,
			HttpComponentsClientHttpRequestFactory requestFactory) {

		val result = super.restTemplate(builder, requestFactory);
		result.setErrorHandler(getErrorHandler());
		return result;
	}
}
