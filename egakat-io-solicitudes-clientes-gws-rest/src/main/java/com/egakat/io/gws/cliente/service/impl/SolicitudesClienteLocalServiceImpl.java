package com.egakat.io.gws.cliente.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.gws.cliente.dto.SolicitudClienteDto;
import com.egakat.io.gws.cliente.service.api.SolicitudesClienteLocalService;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.configuration.properties.SolicitudDespachoClienteRestProperties;

import lombok.val;

@Service
public class SolicitudesClienteLocalServiceImpl extends LocalQueryServiceImpl<SolicitudClienteDto, Integer>
		implements SolicitudesClienteLocalService {

	@Autowired
	private SolicitudDespachoClienteRestProperties properties;

	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected String getResourceName() {
		return IntegracionesRestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected Class<SolicitudClienteDto> getResponseType() {
		return SolicitudClienteDto.class;
	}

	@Override
	protected Class<SolicitudClienteDto[]> getArrayReponseType() {
		return SolicitudClienteDto[].class;
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
	public void aceptar(Integer id) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, "", Object.class, id, "ACEPTADO");
	}

	@Override
	public void rechazar(Integer id, List<ErrorIntegracionDto> errores) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, errores, Object.class, id, "ERROR");
	}
}
