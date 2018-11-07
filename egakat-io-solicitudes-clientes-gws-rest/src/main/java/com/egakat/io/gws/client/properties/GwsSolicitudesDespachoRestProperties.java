package com.egakat.io.gws.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egakat.core.web.client.properties.RestProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = GwsSolicitudesDespachoRestProperties.CONFIGURATION_PROPERTIES)
@Getter
@Setter
@ToString
@Validated
public class GwsSolicitudesDespachoRestProperties implements RestProperties {

	static final String CONFIGURATION_PROPERTIES = "endpoint.gws.solicitudes.despacho.rest";
	
	private String basePath;
}

