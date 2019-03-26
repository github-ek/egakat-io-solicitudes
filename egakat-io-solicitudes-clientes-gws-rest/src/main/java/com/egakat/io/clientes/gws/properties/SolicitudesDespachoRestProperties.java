package com.egakat.io.clientes.gws.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egakat.core.web.client.properties.RestProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = SolicitudesDespachoRestProperties.CONFIGURATION_PROPERTIES)
@Getter
@Setter
@ToString
@Validated
public class SolicitudesDespachoRestProperties implements RestProperties {

	public static final String CONFIGURATION_PROPERTIES = "endpoint.cliente.solicitudes.despacho.rest";
	
	private String basePath;
}

