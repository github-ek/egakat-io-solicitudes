package com.egakat.core.io.stage.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.service.api.PullService;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class PullServiceImpl<I> implements PullService {

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	protected ActualizacionIntegracionCrudService getActualizacionesService() {
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

			val errores = new ArrayList<ErrorIntegracionDto>();
			val actualizacion = asModel(correlacion, input);
			log(actualizacion, i, n);

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

	protected void enqueue(I input, ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			validate(input, actualizacion, errores);

			if (errores.isEmpty()) {
				val discard = discard(input, actualizacion);

				if (!discard) {
					onSuccess(input, actualizacion);
					getActualizacionesService().enqueue(actualizacion);
				} else {
					onDiscarded(input, actualizacion);
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
			log.error("Exception:", e);
		}
	}

	abstract protected ActualizacionIntegracionDto asModel(String correlacion, I input);

	protected void validate(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, ActualizacionIntegracionDto model) {
		boolean result = false;
		if (!result) {
			result = getActualizacionesService().exists(model.getIntegracion(), model.getIdExterno());
		}
		return result;
	}

	protected void onSuccess(I input, ActualizacionIntegracionDto model) {

	}

	protected void onDiscarded(I input, ActualizacionIntegracionDto model) {

	}

	protected void onError(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected void log(ActualizacionIntegracionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}
}