package com.egakat.io.silogtran.components;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.egakat.core.web.client.components.AbstractTokenGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SilogtranTokenGenerator extends AbstractTokenGenerator<JsonNode, String> {

	private static final String FIELD_MSG = "msg";

	private static final String FIELD_CODIGO = "codigo";

	private static final String FIELD_SUCCESS = "success";

	private static final String FIELD_USUARIO = "usuario";

	private static final String FIELD_SECRET = "secret";

	private static final String FIELD_ACCION = "accion";

	private static final String FIELD_SERVICIO = "servicio";

	public static final String TOKEN = "token-silogtran";

	@Value("${com.silogtran.rest.api-service-value}")
	private String apiServiceValue;

	@Value("${com.silogtran.rest.api-action-value}")
	private String apiActionValue;

	@Value("${com.silogtran.rest.api-secret-value}")
	private String apiSecretValue;

	@Autowired
	private SilogtranRestProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public SilogtranRestProperties getProperties() {
		return properties;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Cacheable(cacheNames = TOKEN, unless = "#result == null")
	@Override
	public String token() {
		return super.token();
	}

	@Override
	protected JsonNode getBodyRequest() {
		val result = JsonNodeFactory.instance.objectNode();

		result.put(getProperties().getApiKeyHeader(), getProperties().getApiKeyValue());
		result.put(FIELD_SERVICIO, apiServiceValue);
		result.put(FIELD_ACCION, apiActionValue);
		result.put(FIELD_SECRET, apiSecretValue);

		val usuario = result.putObject(FIELD_USUARIO);
		usuario.put(getProperties().getApiUserHeader(), getProperties().getApiUserValue());
		usuario.put(getProperties().getApiPasswordHeader(), getProperties().getApiPasswordValue());

		return result;
	}

	@Override
	protected ResponseEntity<String> login(String url, HttpEntity<?> entity) {
		val result = getRestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
		return result;
	}

	@Override
	protected String getToken(String response) {
		log.debug("response = {}", response);
		String result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode node = mapper.readTree(response);

			val success = node.get(FIELD_SUCCESS).asBoolean();

			if (success) {
				result = node.findValue(FIELD_CODIGO).asText();
				if (result != null) {
					log.debug(result);
				}
			} else {
				// TODO throw error
				val msg = node.findValue(FIELD_MSG).asText();
				log.error(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO throw error if (result == null)

		return result;
	}

	@CacheEvict(cacheNames = { TOKEN }, allEntries = true)
	@Override
	public void cacheEvict() {

	}
}
