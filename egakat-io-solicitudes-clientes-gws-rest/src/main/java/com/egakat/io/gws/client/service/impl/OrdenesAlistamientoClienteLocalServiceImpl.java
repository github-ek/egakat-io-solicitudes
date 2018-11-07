package com.egakat.io.gws.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.core.web.client.service.impl.LocalQueryServiceImpl;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.client.service.api.OrdenAlistamientoClienteLocalService;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.dto.OrdenAlistamientoClienteDto;

import lombok.val;

//@Service
public class OrdenesAlistamientoClienteLocalServiceImpl extends
		LocalQueryServiceImpl<OrdenAlistamientoClienteDto, Integer> implements OrdenAlistamientoClienteLocalService {

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

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

	@Override
	protected RestClient getRestClient() {
		// TODO Auto-generated method stub
		return null;
	}

}