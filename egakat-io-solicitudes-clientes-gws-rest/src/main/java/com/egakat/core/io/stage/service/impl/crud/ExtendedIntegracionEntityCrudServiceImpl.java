package com.egakat.core.io.stage.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.io.stage.domain.IntegrationEntity;
import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;

public abstract class ExtendedIntegracionEntityCrudServiceImpl<E extends IntegrationEntity, M extends IntegrationEntityDto>
		extends IntegracionEntityCrudServiceImpl<E, M> implements ExtendedIntegracionEntityCrudService<M> {

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