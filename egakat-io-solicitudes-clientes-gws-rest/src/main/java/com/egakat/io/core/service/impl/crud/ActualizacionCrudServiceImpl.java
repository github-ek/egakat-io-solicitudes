package com.egakat.io.core.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.egakat.io.core.domain.Actualizacion;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.repository.ActualizacionRepository;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;

import lombok.val;

@Service
public class ActualizacionCrudServiceImpl extends IntegracionEntityCrudServiceImpl<Actualizacion, ActualizacionDto>
		implements ActualizacionCrudService {

	@Autowired
	private ActualizacionRepository repository;

	@Override
	protected ActualizacionRepository getRepository() {
		return repository;
	}

	@Override
	protected ActualizacionDto asModel(Actualizacion entity) {
		val model = newModel();

		mapModel(entity, model);

		model.setId(entity.getId());
		model.setIntegracion(entity.getIntegracion());
		model.setCorrelacion(entity.getCorrelacion());
		model.setIdExterno(entity.getIdExterno());

		model.setEstadoIntegracion(entity.getEstadoIntegracion());
		model.setSubEstadoIntegracion(entity.getSubEstadoIntegracion());
		model.setEstadoNotificacion(entity.getEstadoNotificacion());
		model.setEntradasEnCola(entity.getEntradasEnCola());
		model.setReintentos(entity.getReintentos());

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

		return model;
	}

	@Override
	protected ActualizacionDto newModel() {
		return new ActualizacionDto();
	}

	@Override
	protected Actualizacion mergeEntity(ActualizacionDto model, Actualizacion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());

		entity.setEstadoIntegracion(model.getEstadoIntegracion());
		entity.setSubEstadoIntegracion(model.getSubEstadoIntegracion());
		entity.setEstadoNotificacion(model.getEstadoNotificacion());
		entity.setEntradasEnCola(model.getEntradasEnCola());
		entity.setReintentos(model.getReintentos());

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
	protected Actualizacion newEntity() {
		return new Actualizacion();
	}

	@Override
	public List<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(String integracion,
			EstadoIntegracionType estadoIntegracion, String... subEstadosIntegracion) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(integracion,
				estadoIntegracion, subEstadosIntegracion);
		val result = asModels(entities);
		return result;
	}

	@Override
	public Slice<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionIn(String integracion,
			EstadoIntegracionType[] estados, Pageable pageable) {
		val slice = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estados, pageable);
		val result = asModels(slice, pageable);
		return result;
	}

	@Override
	public Slice<ActualizacionDto> findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(String integracion,
			EstadoIntegracionType estadoIntegracion, String subEstadoIntegracion, Pageable pageable) {
		val slice = getRepository().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracion(integracion,
				estadoIntegracion, subEstadoIntegracion, pageable);
		val result = asModels(slice, pageable);
		return result;
	}

	@Override
	public List<ActualizacionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
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
	public void enqueue(ActualizacionDto model) {
		// @formatter:off
		val optional = getRepository().findAllByIntegracionAndIdExterno(model.getIntegracion(), model.getIdExterno())
				.sorted((a,b)-> Long.compare(b.getId(), a.getId()))
				.findFirst();
		// @formatter:on

		if (!optional.isPresent()) {
			create(model);
		} else {
			val entity = optional.get();
			if (EstadoIntegracionType.PROCESADO.equals(entity.getEstadoIntegracion())) {
				entity.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
				entity.setEntradasEnCola(0);
				entity.setReintentos(0);
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
	public ActualizacionDto update(ActualizacionDto model, EstadoIntegracionType estado,
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
	public ActualizacionDto updateEstadoNotificacion(ActualizacionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error) {
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