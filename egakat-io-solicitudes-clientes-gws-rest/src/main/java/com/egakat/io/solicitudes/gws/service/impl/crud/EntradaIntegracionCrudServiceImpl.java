package com.egakat.io.solicitudes.gws.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.solicitudes.gws.domain.EntradaIntegracion;
import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;
import com.egakat.io.solicitudes.gws.repository.EntradaIntegracionRepository;
import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;

import lombok.val;

@Service
public class EntradaIntegracionCrudServiceImpl extends CrudServiceImpl<EntradaIntegracion, EntradaIntegracionDto, Long>
		implements EntradaIntegracionCrudService {

	@Autowired
	private EntradaIntegracionRepository repository;

	@Override
	protected EntradaIntegracionRepository getRepository() {
		return repository;
	}

	@Override
	protected EntradaIntegracionDto asModel(EntradaIntegracion entity) {
		// @formatter:off
		val result = EntradaIntegracionDto
				.builder()
				.id(entity.getId())
				.estado(entity.getEstado())
				.programarNotificacion(entity.isProgramarNotificacion())
				.notificacionRealizada(entity.isNotificacionRealizada())
				.integracion(entity.getIntegracion())
				.idExterno(entity.getIdExterno())
				.estadoExterno(entity.getEstadoExterno())
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
	protected EntradaIntegracion asEntity(EntradaIntegracionDto model, EntradaIntegracion entity) {

		entity.setEstado(model.getEstado());
		entity.setProgramarNotificacion(model.isProgramarNotificacion());
		entity.setNotificacionRealizada(model.isNotificacionRealizada());
		entity.setIntegracion(model.getIntegracion());
		entity.setIdExterno(model.getIdExterno());
		entity.setEstadoExterno(model.getEstadoExterno());
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
	protected EntradaIntegracion newEntity() {
		return new EntradaIntegracion();
	}

	@Override
	public List<EntradaIntegracionDto> findAllByIntegracionAndEstado(String integracion,
			EstadoEntradaIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoIn(integracion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<EntradaIntegracionDto> findAllByIntegracionAndEstado(String integracion, boolean programarNotificacion,
			EstadoEntradaIntegracionType... estados) {
		val entities = getRepository().findAllByIntegracionAndProgramarNotificacionAndEstadoIn(integracion,
				programarNotificacion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public void enqueue(EntradaIntegracionDto model) {
		val optional = getRepository().findByIntegracionAndIdExterno(model.getIntegracion(), model.getIdExterno());
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
}