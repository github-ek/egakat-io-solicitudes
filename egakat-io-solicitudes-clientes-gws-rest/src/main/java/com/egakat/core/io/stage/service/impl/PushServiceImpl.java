package com.egakat.core.io.stage.service.impl;

import static com.egakat.core.io.stage.enums.EstadoIntegracionType.ERROR_CARGUE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.service.api.PushService;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ExtendedIntegracionEntityCrudService;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class PushServiceImpl<M extends IntegrationEntityDto, O, R> implements PushService {

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

	abstract protected ExtendedIntegracionEntityCrudService<M> getCrudService();

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	protected String getUrl() {
		val result = getProperties().getBasePath() + getApiEndPoint();
		return result;
	}

	abstract protected String getIntegracion();

	@Override
	public void push() {
		val actualizaciones = getPendientes();

		int i = 1;
		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();
			val model = getModel(actualizacion, errores);

			if (errores.isEmpty()) {
				push(model, actualizacion, errores);
			}

			if (!errores.isEmpty()) {
				onError(model, actualizacion, errores);
				try {
					getCrudService().update(model, actualizacion, ERROR_CARGUE, errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	abstract protected List<ActualizacionIntegracionDto> getPendientes();

	protected void push(M model, ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			O output = asOutput(model);
			validate(output, model, errores);

			if (errores.isEmpty()) {
				val discard = discard(output, model, errores);

				if (errores.isEmpty()) {
					if (!discard) {
						R response = push(output, model, errores);

						if (errores.isEmpty()) {
							onSuccess(response, output, model, actualizacion);
							getCrudService().update(model, actualizacion, actualizacion.getEstadoIntegracion());
						}
					} else {
						onDiscarded(output, model, actualizacion);
						getCrudService().update(model, actualizacion, actualizacion.getEstadoIntegracion());
					}
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
			log.error("Exception:", e);
			e.printStackTrace();
		}
	}

	abstract protected M getModel(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores);

	abstract protected O asOutput(M model);

	protected void validate(O output, M model, List<ErrorIntegracionDto> errores) {

	};

	protected boolean discard(O output, M model, List<ErrorIntegracionDto> errores) {
		return false;
	}

	abstract protected R push(O output, M model, List<ErrorIntegracionDto> errores);

	protected void onSuccess(R response, O output, M model, ActualizacionIntegracionDto actualizacion) {

	}

	protected void onDiscarded(O output, M model, ActualizacionIntegracionDto actualizacion) {

	}

	protected void onError(M model, ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {

	}

	protected void log(ActualizacionIntegracionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}
}