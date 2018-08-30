package com.egakat.io.solicitudes.gws.service.impl.cliente.solicitudes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.solicitudes.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.solicitudes.gws.configuration.properties.SolicitudDespachoClienteRestProperties;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.cliente.solicitudes.SolicitudClienteDto;
import com.egakat.io.solicitudes.gws.service.api.cliente.solicitudes.SolicitudClienteLocalService;

import lombok.val;

@Service
public class SolicitudClienteLocalServiceImpl extends LocalQueryServiceImpl<SolicitudClienteDto, Integer>
		implements SolicitudClienteLocalService {

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
