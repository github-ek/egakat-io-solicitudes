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
		// @formatter:off
		val result = ActualizacionIntegracionDto
				.builder()
				.id(entity.getId())
				.integracion(entity.getIntegracion())
				.idExterno(entity.getIdExterno())
				.correlacion(entity.getCorrelacion())
				.estadoIntegracion(entity.getEstadoIntegracion())
				.estadoNotificacion(entity.getEstadoNotificacion())
				.entradasEnCola(entity.getEntradasEnCola())
				.arg0(entity.getArg0())
				.arg1(entity.getArg1())
				.arg2(entity.getArg2())
				.arg3(entity.getArg3())
				.arg4(entity.getArg4())
				.arg5(entity.getArg5())
				.arg6(entity.getArg6())
				.arg7(entity.getArg7())
				.arg8(entity.getArg8())
				.arg9(entity.getArg9())
				.datos(entity.getDatos())
				.version(entity.getVersion())
				.fechaCreacion(entity.getFechaCreacion())
				.fechaModificacion(entity.getFechaModificacion())
				.build();
		// @formatter:on
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

		if (errores.isEmpty()) {
			throw new RuntimeException(
					"La colección de errores esta vacía. Se requiere de al menos un error para invoar este metodo");
		}

		getErroresService().create(errores);
		model.setEstadoIntegracion(estado);
		model.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);
		val result = update(model);
		return result;
	}

	@Override
	public void updateEstadoNotificacion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error) {
		if (errores.isEmpty()) {
			model.setEstadoNotificacion(ok);
			// TODO ¿en este punto se debe programar una notificacion?
		} else {
			model.setEstadoNotificacion(error);
			getErroresService().create(errores);
		}
		update(model);
	}

}