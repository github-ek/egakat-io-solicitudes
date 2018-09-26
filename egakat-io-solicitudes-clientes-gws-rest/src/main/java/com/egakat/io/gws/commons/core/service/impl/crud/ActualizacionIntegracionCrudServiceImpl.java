package com.egakat.io.gws.commons.core.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.commons.core.domain.ActualizacionIntegracion;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.repository.ActualizacionIntegracionRepository;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;

import lombok.val;

@Service
public class ActualizacionIntegracionCrudServiceImpl
		extends IntegrationEntityCrudServiceImpl<ActualizacionIntegracion, ActualizacionIntegracionDto>
		implements ActualizacionIntegracionCrudService {

	@Autowired
	private ActualizacionIntegracionRepository repository;

	@Override
	protected ActualizacionIntegracionRepository getRepository() {
		return repository;
	}

	@Override
	protected ActualizacionIntegracionDto asModel(ActualizacionIntegracion entity) {
		val result = new ActualizacionIntegracionDto();

		result.setId(entity.getId());
		result.setIntegracion(entity.getIntegracion());
		result.setIdExterno(entity.getIdExterno());
		result.setCorrelacion(entity.getCorrelacion());
		result.setEstadoIntegracion(entity.getEstadoIntegracion());
		result.setEstadoNotificacion(entity.getEstadoNotificacion());
		result.setEntradasEnCola(entity.getEntradasEnCola());
		result.setArg0(entity.getArg0());
		result.setArg1(entity.getArg1());
		result.setArg2(entity.getArg2());
		result.setArg3(entity.getArg3());
		result.setArg4(entity.getArg4());
		result.setArg5(entity.getArg5());
		result.setArg6(entity.getArg6());
		result.setArg7(entity.getArg7());
		result.setArg8(entity.getArg8());
		result.setArg9(entity.getArg9());
		result.setDatos(entity.getDatos());
		result.setVersion(entity.getVersion());
		result.setFechaCreacion(entity.getFechaCreacion());
		result.setFechaModificacion(entity.getFechaModificacion());

		return result;
	}

	@Override
	protected ActualizacionIntegracion mergeEntity(ActualizacionIntegracionDto model, ActualizacionIntegracion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setIdExterno(model.getIdExterno());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setEstadoIntegracion(model.getEstadoIntegracion());
		entity.setEstadoNotificacion(model.getEstadoNotificacion());
		entity.setEntradasEnCola(model.getEntradasEnCola());
		entity.setArg0(model.getArg0());
		entity.setArg1(model.getArg1());
		entity.setArg2(model.getArg2());
		entity.setArg3(model.getArg3());
		entity.setArg4(model.getArg4());
		entity.setArg5(model.getArg5());
		entity.setArg6(model.getArg6());
		entity.setArg7(model.getArg7());
		entity.setArg8(model.getArg8());
		entity.setArg9(model.getArg9());
		entity.setDatos(model.getDatos());

		return entity;
	}

	@Override
	protected ActualizacionIntegracion newEntity() {
		return new ActualizacionIntegracion();
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estado) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(integracion,
				estado, EstadoNotificacionType.NOTIFICAR);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<String> findAllCorrelacionesByEstadoIntegracionIn(List<EstadoIntegracionType> estados) {
		val result = getRepository().findAllCorrelacionesByEstadoIntegracionIn(estados);
		return result;
	}

	@Override
	public void enqueue(ActualizacionIntegracionDto model) {
		// @formatter:off
		val optional = getRepository().findAllByIntegracionAndIdExterno(model.getIntegracion(), model.getIdExterno())
				.sorted((a,b)-> Long.compare(b.getId(), a.getId()))
				.findFirst();
		// @formatter:on

		if (!optional.isPresent()) {
			create(model);
		} else {
			val entity = optional.get();
			if (entity.getEntradasEnCola() == 0) {
				entity.setEntradasEnCola(entity.getEntradasEnCola() + 1);
				getRepository().saveAndFlush(entity);
			}
		}
	}

	@Override
	public ActualizacionIntegracionDto update(ActualizacionIntegracionDto model, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		if (!estado.isError()) {
			val format = "Se esta intentado actualizar al estado %s. Esta operación no admite estados que no sean de error";
			throw new RuntimeException(String.format(format, estado.toString()));
		}

		if (errores.isEmpty()) {
			throw new RuntimeException(
					"La colección de errores esta vacía. Se requiere al menos de un error para realizar esta operación");
		}

		model.setEstadoIntegracion(estado);
		model.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		getErroresService().create(errores);
		val result = update(model);
		return result;
	}

	@Override
	public ActualizacionIntegracionDto updateEstadoNotificacion(ActualizacionIntegracionDto model,
			List<ErrorIntegracionDto> errores, EstadoNotificacionType ok, EstadoNotificacionType error) {
		if (errores.isEmpty()) {
			model.setEstadoNotificacion(ok);
			// TODO ¿en este punto se debe programar una notificacion?
		} else {
			model.setEstadoNotificacion(error);
			getErroresService().create(errores);
		}

		val result = update(model);
		return result;
	}
}