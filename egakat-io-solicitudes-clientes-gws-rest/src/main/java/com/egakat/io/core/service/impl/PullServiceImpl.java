package com.egakat.io.core.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.PullService;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class PullServiceImpl<I> implements PullService {

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	protected ActualizacionCrudService getActualizacionesService() {
		return actualizacionesService;
	}

	protected ErrorIntegracionCrudService getErroresService() {
		return erroresService;
	}

	protected abstract String getIntegracion();

	protected String defaultCorrelacion() {
		val result = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
		return result;
	}

	protected void enqueue(String correlacion, List<I> inputs) {
		int i = 1;
		int n = inputs.size();
		for (val input : inputs) {
			val actualizacion = asModel(correlacion, input);
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();

			enqueue(input, actualizacion, errores);

			if (!errores.isEmpty()) {
				onError(input, actualizacion, errores);

				try {
					getActualizacionesService().update(actualizacion, EstadoIntegracionType.ERROR_ESTRUCTURA, errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	protected void enqueue(I input, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			validate(input, actualizacion, errores);

			if (errores.isEmpty()) {
				val discard = discard(input, actualizacion);

				if (!discard) {
					onSuccess(input, actualizacion);
					getActualizacionesService().enqueue(actualizacion);
				} else {
					onDiscard(input, actualizacion);
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
		}
	}

	abstract protected ActualizacionDto asModel(String correlacion, I input);

	protected void validate(I input, ActualizacionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, ActualizacionDto model) {
		return false;
	}

	protected void onSuccess(I input, ActualizacionDto model) {

	}

	protected void onDiscard(I input, ActualizacionDto model) {

	}

	protected void onError(I input, ActualizacionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected void log(ActualizacionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}
}