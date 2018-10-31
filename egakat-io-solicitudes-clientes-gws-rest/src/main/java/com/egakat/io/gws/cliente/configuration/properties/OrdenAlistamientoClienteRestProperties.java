package com.egakat.io.gws.cliente.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egakat.core.web.client.properties.RestProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "com.cliente.integraciones.ordenes.alistamiento.rest")
@Getter
@Setter
@ToString
@Validated
public class OrdenAlistamientoClienteRestProperties implements RestProperties {

	private String basePath;
}

