package com.egakat.io.silogtran.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.egakat.core.web.client.components.AbstractRestClientImpl;

import lombok.val;

@Component
public class SilogtranRestClient extends AbstractRestClientImpl {

	@Autowired
	private SilogtranRestProperties properties;


	@Autowired
	private RestTemplate restTemplate;


	@Autowired
	private SilogtranTokenGenerator tokenGenerator;

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Override
	protected MultiValueMap<String, String> getDefaultHeaders() {
		val token = tokenGenerator.token();

		val result = new LinkedMultiValueMap<String, String>();
		result.add(properties.getApiTokenHeader(), token);
		return result;
	}
}
