package com.egakat.io.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.PushService;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PushServiceImpl<M extends IntegracionEntityDto, O, R> implements PushService {

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

	protected abstract ExtendedIntegracionEntityCrudService<M> getCrudService();

	@Override
	public void push() {
		val actualizaciones = getPendientes();

		int i = 1;
		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();

			push(actualizacion, errores);

			if (!errores.isEmpty()) {
				if (hasErrors(actualizacion, errores)) {
					onError(actualizacion, errores);
				} else {
					onRetry(actualizacion, errores);
				}

				try {
					getActualizacionesService().update(actualizacion, actualizacion.getEstadoIntegracion(), errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	protected abstract List<ActualizacionDto> getPendientes();

	protected void push(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			val model = getModel(actualizacion, errores);

			if (errores.isEmpty()) {
				O output = asOutput(model, actualizacion, errores);

				if (errores.isEmpty()) {
					validateOutput(output, model, actualizacion, errores);

					if (errores.isEmpty()) {
						val discard = discard(output, model, actualizacion);

						if (!discard) {
							R response = push(output, model, actualizacion, errores);

							if (errores.isEmpty()) {
								onSuccess(response, output, model, actualizacion);
								getCrudService().update(model, actualizacion, actualizacion.getEstadoIntegracion());
							}
						} else {
							onDiscard(output, model, actualizacion);
							getActualizacionesService().update(actualizacion);
						}
					}
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);

			if (retry(e, actualizacion, errores)) {
				actualizacion.setReintentos(actualizacion.getReintentos());
			}
		}
	}

	protected abstract M getModel(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected abstract O asOutput(M model, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void validateOutput(O output, M model, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(O output, M model, ActualizacionDto actualizacion) {
		return false;
	}

	protected boolean retry(RuntimeException e, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		boolean result = false;

		if (e instanceof HttpStatusCodeException) {
			if (((HttpStatusCodeException) e).getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
				result = true;
			}
		}

		return result;
	}

	protected abstract R push(O output, M model, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void onSuccess(R response, O output, M model, ActualizacionDto actualizacion) {
		actualizacion.setEstadoIntegracion(EstadoIntegracionType.CARGADO);
		actualizacion.setReintentos(0);
	}

	protected void onDiscard(O output, M model, ActualizacionDto actualizacion) {
		actualizacion.setSubEstadoIntegracion("DESCARTADO");
	}

	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setEstadoIntegracion(EstadoIntegracionType.ERROR_CARGUE);
		actualizacion.setReintentos(0);
	}

	protected void onRetry(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {

	}

	protected boolean hasErrors(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		boolean result = false;
		if (!errores.isEmpty()) {
			if (actualizacion.getReintentos() == 0 || actualizacion.getReintentos() < getNumeroMaximoIntentos()) {
				result = true;
			}
		}
		return result;
	}

	protected int getNumeroMaximoIntentos() {
		return 30;
	}

	protected void log(ActualizacionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}

}