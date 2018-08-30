package com.egakat.io.solicitudes.gws.service.impl.cliente.ordenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.solicitudes.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.solicitudes.gws.configuration.properties.OrdenAlistamientoClienteRestProperties;
import com.egakat.io.solicitudes.gws.dto.cliente.ordenes.OrdenAlistamientoClienteDto;
import com.egakat.io.solicitudes.gws.service.api.cliente.ordenes.OrdenAlistamientoClienteLocalService;

import lombok.val;

@Service
public class OrdenesAlistamientoClienteLocalServiceImpl extends
		LocalQueryServiceImpl<OrdenAlistamientoClienteDto, Integer> implements OrdenAlistamientoClienteLocalService {

	@Autowired
	private OrdenAlistamientoClienteRestProperties properties;

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected String getResourceName() {
		return IntegracionesRestConstants.ORDENES_ALISTAMIENTO;
	}

	@Override
	protected Class<OrdenAlistamientoClienteDto> getResponseType() {
		return OrdenAlistamientoClienteDto.class;
	}

	@Override
	protected Class<OrdenAlistamientoClienteDto[]> getArrayReponseType() {
		return OrdenAlistamientoClienteDto[].class;
	}

	@Override
	public Integer upload(OrdenAlistamientoClienteDto model) {
		val response = getRestClient().post(getResourcePath(), model, Integer.class);
		val result = response.getBody();
		return result;
	}

}