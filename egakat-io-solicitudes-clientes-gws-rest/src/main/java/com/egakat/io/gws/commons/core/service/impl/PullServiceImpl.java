package com.egakat.io.gws.commons.core.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.service.api.PullService;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.IntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class PullServiceImpl<I, M extends IntegrationEntityDto> implements PullService {

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

	abstract protected IntegracionEntityCrudService<M> getCrudService();

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	abstract protected String getIntegracion();

	protected String defaultCorrelacion() {
		val result = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
		return result;
	}

	protected void enqueue(String correlacion, List<I> inputs) {
		int i = 1;
		int n = inputs.size();
		for (val input : inputs) {
			try {
				val model = asModel(correlacion, input);

				log(model, i, n);

				val errores = new ArrayList<ErrorIntegracionDto>();

				enqueue(input, model, errores);

				if (errores.size() > 0) {
					onError(input, model, errores);
					getActualizacionesService().update(model, errores);
				}
			} catch (RuntimeException e) {
				log.error("Exception:", e);
			}

			i++;
		}
	}

	abstract protected ActualizacionIntegracionDto asModel(String correlacion, I input);

	protected void enqueue(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {
		try {
			validate(input, model, errores);
			if (errores.isEmpty()) {

				val discard = discard(input, model, errores);
				if (errores.isEmpty()) {
					if (!discard) {
						onSuccess(input, model);
						getActualizacionesService().enqueue(model);
					} else {
						onDiscarded(input, model);
					}
				}
			}
		} catch (RuntimeException e) {
			errores.add(getErroresService().error(model, "", e));
		}
	}

	protected void validate(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {
		boolean result = getCrudService().exists(model.getIntegracion(), model.getIdExterno());
		if (!result) {
			result = getActualizacionesService().exists(model.getIntegracion(), model.getIdExterno());
		}

		return false;
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