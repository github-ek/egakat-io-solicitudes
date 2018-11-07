package com.egakat.io.core.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.io.core.domain.IntegracionEntity;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;

public abstract class ExtendedIntegracionEntityCrudServiceImpl<E extends IntegracionEntity, M extends IntegracionEntityDto>
		extends IntegracionEntityCrudServiceImpl<E, M> implements ExtendedIntegracionEntityCrudService<M> {

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	public ActualizacionCrudService getActualizacionesService() {
		return actualizacionesService;
	}

	@Override
	public M create(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado) {
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
	public M create(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		getActualizacionesService().update(actualizacion, estado, errores);
		val result = create(model);
		return result;
	}

	@Override
	public M update(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado) {
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
	public M update(M model, ActualizacionDto actualizacion, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		getActualizacionesService().update(actualizacion, estado, errores);
		val result = update(model);
		return result;
	}
}