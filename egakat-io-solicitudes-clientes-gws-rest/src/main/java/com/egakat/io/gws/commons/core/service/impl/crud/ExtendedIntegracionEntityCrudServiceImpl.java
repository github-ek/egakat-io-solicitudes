package com.egakat.io.gws.commons.core.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.io.gws.commons.core.domain.IntegrationEntity;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;

public abstract class ExtendedIntegracionEntityCrudServiceImpl<E extends IntegrationEntity, M extends IntegrationEntityDto>
		extends IntegrationEntityCrudServiceImpl<E, M> implements ExtendedIntegracionEntityCrudService<M> {

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	public ActualizacionIntegracionCrudService getActualizacionesService() {
		return actualizacionesService;
	}

	@Override
	public M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado) {
		if (estado.isError()) {
			val format = "Se esta intentado actualizar al estado %s. Esta operación no admite estados de error";
			throw new RuntimeException(String.format(format, estado.toString()));
		}

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		getActualizacionesService().update(actualizacion);
		val result = create(model);
		return result;
	}

	@Override
	public M create(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		getActualizacionesService().update(actualizacion, estado, errores);
		val result = create(model);
		return result;
	}

	@Override
	public M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado) {
		if (estado.isError()) {
			val format = "Se esta intentado actualizar al estado %s. Esta operación no admite estados de error";
			throw new RuntimeException(String.format(format, estado.toString()));
		}

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		getActualizacionesService().update(actualizacion);
		val result = update(model);
		return result;
	}

	@Override
	public M update(M model, ActualizacionIntegracionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		getActualizacionesService().update(actualizacion, estado, errores);
		val result = update(model);
		return result;
	}
}