package com.egakat.io.gws.commons.core.service.impl.crud;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.left;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.gws.commons.core.domain.ErrorIntegracion;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.repository.ErrorIntegracionRepository;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;

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
		ErrorIntegracionDto model = new ErrorIntegracionDto();

		model.setId(entity.getId());
		model.setIntegracion(entity.getIntegracion());
		model.setCorrelacion(entity.getCorrelacion());
		model.setIdExterno(entity.getIdExterno());
		model.setEstadoNotificacion(entity.getEstadoNotificacion());
		model.setFechaNotificacion(entity.getFechaNotificacion());
		model.setCodigo(entity.getCodigo());
		model.setMensaje(entity.getMensaje());
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
		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	@Override
	protected ErrorIntegracion mergeEntity(ErrorIntegracionDto model, ErrorIntegracion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());
		entity.setEstadoNotificacion(model.getEstadoNotificacion());
		entity.setFechaNotificacion(model.getFechaNotificacion());
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
	public List<ErrorIntegracionDto> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion,
			String idExterno, String correlacion) {
		val entities = getRepository().findAllByIntegracionAndIdExternoAndCorrelacion(integracion, idExterno,
				correlacion);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<ErrorIntegracionDto> findAllByEstadoNotificacionIn(String integracion,
			EstadoNotificacionType... estados) {
		val entities = getRepository().findAllByIntegracionAndEstadoNotificacionIn(integracion, estados);
		val result = asModels(entities);
		return result;
	}

	@Override
	public ErrorIntegracionDto error(String integracion, String correlacion, String idExterno, String codigo,
			String mensaje, String... arg) {
		val argumentos = normalizarArgumentos(arg);

		// @formatter:off
		val result = ErrorIntegracionDto
				.builder()
				.integracion(integracion)
				.correlacion(correlacion)
				.idExterno(idExterno)
				.estadoNotificacion(EstadoNotificacionType.NOTIFICAR)
				.codigo(left(codigo, 100))
				.mensaje(mensaje)
				.arg0(argumentos[0])
				.arg1(argumentos[1])
				.arg2(argumentos[2])
				.arg3(argumentos[3])
				.arg4(argumentos[4])
				.arg5(argumentos[5])
				.arg6(argumentos[6])
				.arg7(argumentos[7])
				.arg8(argumentos[8])
				.arg9(argumentos[9])				
				.build();
		// @formatter:on
		return result;
	}

	@Override
	public ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, String mensaje, String... arg) {
		val result = error(model.getIntegracion(), model.getCorrelacion(), model.getIdExterno(), codigo, mensaje, arg);
		return result;
	}

	@Override
	public ErrorIntegracionDto error(String integracion, String correlacion, String idExterno, String codigo,
			Throwable t) {
		val c = isEmpty(codigo) ? t.getClass().getName() : codigo;
		String msg = t.getMessage();
		if (t instanceof RestClientResponseException) {
			msg = ((RestClientResponseException) t).getResponseBodyAsString();
		}
		val result = error(integracion, correlacion, idExterno, c, msg);
		return result;
	}

	@Override
	public ErrorIntegracionDto error(IntegrationEntityDto model, String codigo, Throwable t) {
		val result = error(model.getIntegracion(), model.getCorrelacion(), model.getIdExterno(), codigo, t);
		return result;
	}

	@Override
	public void create(String integracion, String correlacion, String idExterno, String codigo, String mensaje,
			String... arg) {
		val model = error(integracion, correlacion, idExterno, codigo, mensaje, arg);
		create(model);
	}

	@Override
	public void create(IntegrationEntityDto model, String codigo, String mensaje, String... arg) {
		create(model.getIntegracion(), model.getCorrelacion(), model.getIdExterno(), codigo, mensaje, arg);
	}

	@Override
	public void create(String integracion, String correlacion, String idExterno, String codigo, Throwable t) {
		val c = isEmpty(codigo) ? t.getClass().getName() : codigo;
		String msg = t.getMessage();
		if (t instanceof RestClientResponseException) {
			msg = ((RestClientResponseException) t).getResponseBodyAsString();
		}
		create(integracion, correlacion, idExterno, c, msg);
	}

	@Override
	public void create(IntegrationEntityDto model, String codigo, Throwable t) {
		create(model.getIntegracion(), model.getCorrelacion(), model.getIdExterno(), codigo, t);
	}

	protected static String[] normalizarArgumentos(String... arg) {
		val result = new String[10];

		int n = Math.min(result.length, arg.length);
		for (int i = 0; i < result.length; i++) {
			if (i < n) {
				result[i] = arg[i];
			} else {
				result[i] = "";
			}
		}

		return result;
	}

}