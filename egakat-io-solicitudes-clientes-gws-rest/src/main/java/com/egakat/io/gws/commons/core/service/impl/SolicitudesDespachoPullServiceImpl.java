package com.egakat.io.gws.commons.core.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.IntegracionEntityCrudService;
import com.egakat.io.gws.solicitudes.service.api.SolicitudClienteLocalService;
import com.egakat.io.gws.solicitudes.service.api.SolicitudesDespachoPullService;

import lombok.val;

@Service
public class SolicitudesDespachoPullServiceImpl extends PullServiceImpl<Integer, ActualizacionIntegracionDto>
		implements SolicitudesDespachoPullService {

	@Autowired
	private SolicitudClienteLocalService localService;

	@Autowired
	private ActualizacionIntegracionCrudService entradasService;

	@Override
	public void pull() {
		val ids = localService.getAllByStatus("ENVIAR");
		val correlacion = LocalDateTime.now().toString();
		for (val id : ids) {
			val model = asEntradaIntegracion(id, correlacion);

			entradasService.enqueue(model);
		}

	}

	protected ActualizacionIntegracionDto asEntradaIntegracion(Integer id, String correlacion) {
		// @formatter:off
		val result = ActualizacionIntegracionDto
				.builder()
				.integracion(getIntegracion())
				.idExterno(id.toString())
				.correlacion(correlacion)
				.estadoExterno("")
				.estadoIntegracion(EstadoIntegracionType.NO_PROCESADO)
				.build();				
		// @formatter:on

		return result;
	}

	@Override
	protected IntegracionEntityCrudService<ActualizacionIntegracionDto> getCrudService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RestProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RestClient getRestClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getApiEndPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getIntegracion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActualizacionIntegracionDto asModel(String correlacion, Integer input) {
		// TODO Auto-generated method stub
		return null;
	}

}
