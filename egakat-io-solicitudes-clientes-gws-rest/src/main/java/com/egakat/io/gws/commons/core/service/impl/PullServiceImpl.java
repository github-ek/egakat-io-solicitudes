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
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
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

	abstract protected String getIntegracion();

	abstract protected String getApiEndPoint();

	abstract protected String getQuery();

	protected String defaultCorrelacion() {
		val result = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
		return result;
	}

	protected void enqueue(String correlacion, List<I> inputs) {

		int i = 1;
		int n = inputs.size();
		for (val input : inputs) {
			val model = asModel(correlacion, input);
			val errores = new ArrayList<ErrorIntegracionDto>();
			
			log(model, i, n);
			try {
				validate(input, model, errores);
				
				if (errores.isEmpty()) {
					val discard = discard(input, model);
					
					if (discard) {
						onDiscarded(input, model);
					} else {
						onSuccess(input, model);
						getActualizacionesService().enqueue(model);
					}
				}
			} catch (RuntimeException e) {
				val error = getErroresService().error(model, "", e);
				errores.add(error);
				log.error("Exception:", e);
			}

			if (!errores.isEmpty()) {
				onError(input, model, errores);
				try {
					getActualizacionesService().update(model, EstadoIntegracionType.ERROR_ESTRUCTURA, errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	abstract protected ActualizacionIntegracionDto asModel(String correlacion, I input);

	protected void validate(I input, ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, ActualizacionIntegracionDto model) {
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