package com.egakat.io.gws.client.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.egakat.core.web.client.components.AbstractRestClientImpl;

@Component
public class GwsRestClient extends AbstractRestClientImpl {

	@Autowired 
	private RestTemplate restTemplate;
	
	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
