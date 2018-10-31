package com.egakat.core.io.stage.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.left;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.service.api.DownloadService;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class DownloadServiceImpl<I, M extends IntegrationEntityDto> implements DownloadService {

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
				if (!actualizacion.getEstadoIntegracion().isReintento()) {
					actualizacion.setEstadoIntegracion(EstadoIntegracionType.ERROR_ESTRUCTURA);
				}

				onError(actualizacion, errores);
				try {
					getActualizacionesService().update(actualizacion, actualizacion.getEstadoIntegracion(), errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	protected List<ActualizacionIntegracionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionIn(getIntegracion(),
				EstadoIntegracionType.NO_PROCESADO, EstadoIntegracionType.REINTENTAR, EstadoIntegracionType.CORREGIDO);
		return result;
	}

	protected void download(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			try {
				I input = getInput(actualizacion, errores);

				if (errores.isEmpty()) {
					validateInput(input, actualizacion, errores);

					if (errores.isEmpty()) {
						M model = asModel(input, actualizacion);
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
								actualizacion.setEstadoIntegracion(EstadoIntegracionType.DESCARTADO);
								onDiscarded(input, model, actualizacion);
								getActualizacionesService().update(actualizacion);
							}
						}
					}
				}
			} catch (HttpStatusCodeException e) {
				if (e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
					retry(e, actualizacion, errores);
				} else {
					throw e;
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
		}
	}

	protected void retry(HttpStatusCodeException e, ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val error = getErroresService().error(actualizacion, "", e);
		errores.add(error);

		actualizacion.setEstadoIntegracion(EstadoIntegracionType.REINTENTAR);
		actualizacion.setSubEstadoIntegracion("");
	}

	abstract protected I getInput(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void validateInput(I input, ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {

	}

	abstract protected M asModel(I input, ActualizacionIntegracionDto actualizacion);

	protected void validateModel(M model, ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {

	}

	protected boolean discard(I input, M model, ActualizacionIntegracionDto actualizacion) {
		return false;
	}

	protected void onSuccess(I input, M model, ActualizacionIntegracionDto actualizacion) {
		actualizacion.setEstadoIntegracion(EstadoIntegracionType.ESTRUCTURA_VALIDA);
	}

	protected void onError(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {

	}

	protected void onDiscarded(I input, M model, ActualizacionIntegracionDto actualizacion) {

	}

	private void log(ActualizacionIntegracionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}

	protected String normalizar(String str, int len) {
		val result = left(defaultString(str), len);
		return result;
	}
}