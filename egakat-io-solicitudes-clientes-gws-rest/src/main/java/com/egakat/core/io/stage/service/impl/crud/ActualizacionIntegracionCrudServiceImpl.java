package com.egakat.core.io.stage.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.domain.ActualizacionIntegracion;
import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;
import com.egakat.core.io.stage.repository.ActualizacionIntegracionRepository;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;

import lombok.val;

@Service
public class ActualizacionIntegracionCrudServiceImpl
		extends IntegracionEntityCrudServiceImpl<ActualizacionIntegracion, ActualizacionIntegracionDto>
		implements ActualizacionIntegracionCrudService {

	@Autowired
	private ActualizacionIntegracionRepository repository;

	@Override
	protected ActualizacionIntegracionRepository getRepository() {
		return repository;
	}

	@Override
	protected ActualizacionIntegracionDto asModel(ActualizacionIntegracion entity) {
		val model = new ActualizacionIntegracionDto();

		model.setId(entity.getId());
		model.setIntegracion(entity.getIntegracion());
		model.setCorrelacion(entity.getCorrelacion());
		model.setIdExterno(entity.getIdExterno());
		model.setEstadoIntegracion(entity.getEstadoIntegracion());
		model.setSubEstadoIntegracion(entity.getSubEstadoIntegracion());
		model.setEstadoNotificacion(entity.getEstadoNotificacion());
		model.setEntradasEnCola(entity.getEntradasEnCola());
		model.setArg0(entity.getArg0());
		model.setArg1(entity.getArg1());
		model.setArg2(entity.getArg2());
		model.setArg3(entity.getArg3());
		model.setArg4(entity.getArg4());
		model.setArg5(entity.getArg5());
		model.setArg6(entity.getArg6());
		model.setArg7(entity.getArg7());
		model.setArg8(entity.getArg8());
		model.setArg9(entity.getArg9());
		model.setDatos(entity.getDatos());
		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	@Override
	protected ActualizacionIntegracionDto newModel() {
		return new ActualizacionIntegracionDto();
	}
	
	@Override
	protected ActualizacionIntegracion mergeEntity(ActualizacionIntegracionDto model, ActualizacionIntegracion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());
		entity.setEstadoIntegracion(model.getEstadoIntegracion());
		entity.setSubEstadoIntegracion(model.getSubEstadoIntegracion());
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
	public List<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(
			String integracion, EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(integracion,
				estadoIntegracion, subEstadoIntegracion);
		val result = asModels(entities);
		return result;
	}

	@Override
	public Slice<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estados, Pageable pageable) {
		val slice = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estados, pageable);
		val result = asModels(slice, pageable);
		return result;
	}

	@Override
	public Slice<ActualizacionIntegracionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(
			String integracion, EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion,
			Pageable pageable) {
		val slice = getRepository().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(integracion,
				estadoIntegracion, subEstadoIntegracion, pageable);
		val result = asModels(slice, pageable);
		return result;
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(integracion,
				estados, EstadoNotificacionType.NOTIFICAR);
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
			if (EstadoIntegracionType.FINALIZADO.equals(entity.getEstadoIntegracion())) {
				entity.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
				entity.setEntradasEnCola(0);
				getRepository().saveAndFlush(entity);
			} else {
				if (entity.getEntradasEnCola() == 0) {
					entity.setEntradasEnCola(entity.getEntradasEnCola() + 1);
					getRepository().saveAndFlush(entity);
				}
			}
		}
	}

	@Override
	public ActualizacionIntegracionDto update(ActualizacionIntegracionDto model, EstadoIntegracionType estado,
			List<ErrorIntegracionDto> errores) {

		if (!estado.isError() && !estado.isReintento()) {
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
			// TODO se debe hacer un manejo de re intento de notificacion en caso de error,
			// esto se podria manejar mejor en las notificaciones
		}

		val result = update(model);
		return result;
	}
}