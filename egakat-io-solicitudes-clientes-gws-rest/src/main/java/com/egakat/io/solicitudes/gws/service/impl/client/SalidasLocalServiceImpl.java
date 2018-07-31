package com.egakat.io.solicitudes.gws.service.impl.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.solicitudes.gws.components.SalidasRestProperties;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.service.api.client.SalidasLocalService;
import com.gws.integraciones.solicitudes.salidas.constants.RestConstants;
import com.gws.integraciones.solicitudes.salidas.dto.SolicitudDto;

import lombok.val;

@Service
public class SalidasLocalServiceImpl extends LocalQueryServiceImpl<SolicitudDto, Integer>
		implements SalidasLocalService {

	@Autowired
	private SalidasRestProperties properties;

	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected String getResourceName() {
		return RestConstants.SOLICITUDES_SALIDAS;
	}

	@Override
	protected Class<SolicitudDto> getResponseType() {
		return SolicitudDto.class;
	}

	@Override
	protected Class<SolicitudDto[]> getArrayReponseType() {
		return SolicitudDto[].class;
	}

	@Override
	public List<Integer> getAllByStatus(String status) {
		val query = "?status={status}";
		val response = getRestClient().getAllQuery(getResourcePath(), query, Integer[].class, status);
		val result = Arrays.asList(response.getBody());
		return result;
	}

	@Override
	public void confirmarRecibo(Integer id) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, "", Object.class, id, "RECIBIDO");
	}

	@Override
	public void notificarErrores(Integer id, List<ErrorIntegracionDto> errores) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, errores, Object.class, id, "ERROR");
	}
}
