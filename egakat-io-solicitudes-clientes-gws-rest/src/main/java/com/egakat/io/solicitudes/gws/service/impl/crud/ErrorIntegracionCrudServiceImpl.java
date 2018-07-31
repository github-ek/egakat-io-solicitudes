package com.egakat.io.solicitudes.gws.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.solicitudes.gws.domain.ErrorIntegracion;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.repository.ErrorIntegracionRepository;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;

@Service
public class ErrorIntegracionCrudServiceImpl extends CrudServiceImpl<ErrorIntegracion, ErrorIntegracionDto, Long>
		implements ErrorIntegracionCrudService {

	@Autowired
	private ErrorIntegracionRepository repository;

	@Override
	protected ErrorIntegracionRepository getRepository() {
		return repository;
	}

	@Override
	protected ErrorIntegracionDto asModel(ErrorIntegracion entity) {
		// @formatter:off
		val result = ErrorIntegracionDto
				.builder()
				.id(entity.getId())
				.integracion(entity.getIntegracion())
				.idExterno(entity.getIdExterno())
				.codigo(entity.getCodigo())
				.mensaje(entity.getMensaje())
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
				.version(entity.getVersion())
				.fechaCreacion(entity.getFechaCreacion())
				.fechaModificacion(entity.getFechaModificacion())
				.build();
		// @formatter:on
		return result;
	}

	@Override
	protected ErrorIntegracion asEntity(ErrorIntegracionDto model, ErrorIntegracion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setIdExterno(model.getIdExterno());
		entity.setCodigo(model.getCodigo());
		entity.setMensaje(model.getMensaje());
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

		return entity;
	}

	@Override
	protected ErrorIntegracion newEntity() {
		return new ErrorIntegracion();
	}

	@Override
	public List<ErrorIntegracionDto> findAllByIntegracionAndIdExterno(String integracion, String idExterno) {
		val entities = getRepository().findAllByIntegracionAndIdExterno(integracion, idExterno);
		val result = asModels(entities);
		return result;
	}
}