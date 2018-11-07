package com.egakat.io.core.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.left;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.DownloadService;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class DownloadServiceImpl<I, M extends IntegracionEntityDto> implements DownloadService {

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

	abstract protected String getIntegracion();

	abstract protected ExtendedIntegracionEntityCrudService<M> getCrudService();

	@Override
	public void download() {
		val actualizaciones = getPendientes();

		int i = 1;
		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();

			download(actualizacion, errores);

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

	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionIn(getIntegracion(),
				EstadoIntegracionType.NO_PROCESADO, EstadoIntegracionType.CORREGIDO);
		return result;
	}

	protected void download(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			I input = getInput(actualizacion, errores);

			if (errores.isEmpty()) {
				validateInput(input, actualizacion, errores);

				if (errores.isEmpty()) {
					M model = asModel(input, actualizacion, errores);

					if (errores.isEmpty()) {
						validateModel(model, actualizacion, errores);

						if (errores.isEmpty()) {
							val discard = discard(input, model, actualizacion);

							if (!discard) {
								onSuccess(input, model, actualizacion);
								if (model.getId() == null) {
									getCrudService().create(model, actualizacion, actualizacion.getEstadoIntegracion());
								} else {
									getCrudService().update(model, actualizacion, actualizacion.getEstadoIntegracion());
								}
							} else {
								onDiscard(input, model, actualizacion);
								getActualizacionesService().update(actualizacion);
							}
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

	abstract protected I getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void validateInput(I input, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {

	}

	abstract protected M asModel(I input, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void validateModel(M model, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, M model, ActualizacionDto actualizacion) {
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

	protected void onSuccess(I input, M model, ActualizacionDto actualizacion) {
		actualizacion.setEstadoIntegracion(EstadoIntegracionType.ESTRUCTURA_VALIDA);
		actualizacion.setReintentos(0);
	}

	protected void onDiscard(I input, M model, ActualizacionDto actualizacion) {
		actualizacion.setSubEstadoIntegracion("DESCARTADO");
	}

	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setEstadoIntegracion(EstadoIntegracionType.ERROR_ESTRUCTURA);
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

	protected String normalizar(String str, int len) {
		val result = left(defaultString(str), len);
		return result;
	}
}