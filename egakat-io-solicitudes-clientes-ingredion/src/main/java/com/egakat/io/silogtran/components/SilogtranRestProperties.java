package com.egakat.io.silogtran.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.core.web.client.properties.RestTokenGeneratorProperties;
import com.egakat.core.web.client.properties.RestUserPasswordProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = SilogtranRestProperties.configurationPropertiesPrefix)
@Getter
@Setter
@ToString
@Validated
public class SilogtranRestProperties implements RestProperties, RestTokenGeneratorProperties, RestUserPasswordProperties {

	final public static String configurationPropertiesPrefix = "com.silogtran.rest";

	private String basePath;

	private String apiKeyHeader;

	private String apiKeyValue;

	private String apiTokenHeader;

	private String apiTokenValue;

	private String apiTokenUrlGenerator;
	
	private String apiUserHeader;

	private String apiUserValue;

	private String apiPasswordHeader;

	private String apiPasswordValue;
}