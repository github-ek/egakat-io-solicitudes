package com.egakat.io.gws.cliente.service.impl.solicitudes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.gws.cliente.configuration.constants.SolicitudEstadoConstants;
import com.egakat.io.gws.cliente.dto.SolicitudDespachoClienteDto;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesClienteLocalService;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.configuration.properties.SolicitudDespachoClienteRestProperties;

import lombok.val;

@Service
public class SolicitudesClienteLocalServiceImpl extends LocalQueryServiceImpl<SolicitudDespachoClienteDto, Integer>
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
	protected Class<SolicitudDespachoClienteDto> getResponseType() {
		return SolicitudDespachoClienteDto.class;
	}

	@Override
	protected Class<SolicitudDespachoClienteDto[]> getArrayReponseType() {
		return SolicitudDespachoClienteDto[].class;
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
		getRestClient().put(getResourcePath() + query, "", Object.class, id, SolicitudEstadoConstants.RECIBIDA_OPL);
	}

	@Override
	public void aceptar(Integer id) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, "", Object.class, id, SolicitudEstadoConstants.ACEPTADA_OPL);
	}

	@Override
	public void rechazar(Integer id, List<ErrorIntegracionDto> errores) {
		val query = "/{id}?status={status}";
		getRestClient().put(getResourcePath() + query, errores, Object.class, id, SolicitudEstadoConstants.RECHAZADA_OPL);
	}
}
