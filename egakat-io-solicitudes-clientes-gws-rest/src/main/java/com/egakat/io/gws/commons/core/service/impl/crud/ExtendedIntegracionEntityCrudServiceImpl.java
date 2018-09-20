package com.egakat.io.gws.commons.core.service.impl.crud;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.io.gws.commons.core.domain.IntegrationEntity;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;

public abstract class ExtendedIntegracionEntityCrudServiceImpl<E extends IntegrationEntity, M extends IntegrationEntityDto, S>
		extends IntegrationEntityCrudServiceImpl<E, M> implements ExtendedIntegracionEntityCrudService<M, S> {

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	public ActualizacionIntegracionCrudService getActualizacionesService() {
		return actualizacionesService;
	}

	@Override
	public M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado) {
		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		getActualizacionesService().update(actualizacion);
		val result = create(model);
		return result;
	}
	
	@Override
	public M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado) {
		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		getActualizacionesService().update(actualizacion);
		val result = update(model);
		return result;
	}
}