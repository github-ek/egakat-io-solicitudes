package com.egakat.io.gws.commons.core.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.left;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.service.api.DownloadService;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class DownloadServiceImpl<I, M extends IntegrationEntityDto, S> implements DownloadService {

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

	abstract protected ExtendedIntegracionEntityCrudService<M, S> getCrudService();

	abstract protected RestProperties getProperties();

	abstract protected RestClient getRestClient();

	abstract protected String getApiEndPoint();

	abstract protected String getQuery();

	abstract protected String getIntegracion();

	@Override
	public void download() {
		val actualizaciones = getActualizacionesService().findAllByEstadoIntegracionIn(getIntegracion(),
				EstadoIntegracionType.NO_PROCESADO, EstadoIntegracionType.CORREGIDO);

		int i = 1;
		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();

			download(actualizacion, errores);

			if (!errores.isEmpty()) {
				onError(actualizacion, errores);
				try {
					getActualizacionesService().update(actualizacion, EstadoIntegracionType.ERROR_ESTRUCTURA, errores);
				} catch (Exception e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	protected void download(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			val input = getInput(actualizacion, errores);

			if (errores.isEmpty()) {
				validate(actualizacion, input, errores);

				if (errores.isEmpty()) {
					val model = asModel(actualizacion, input);
					onSuccess(actualizacion, input, model);
					getCrudService().create(model, actualizacion, EstadoIntegracionType.ESTRUCTURA_VALIDA);
				}
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
			log.error("Exception:", e);
		}
	}

	abstract protected I getInput(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores);

	protected void validate(ActualizacionIntegracionDto actualizacion, I input, List<ErrorIntegracionDto> errores) {

	}

	abstract protected M asModel(ActualizacionIntegracionDto actualizacion, I input);

	protected void onSuccess(ActualizacionIntegracionDto actualizacion, I input, M model) {

	}

	protected void onError(ActualizacionIntegracionDto actualizacion, ArrayList<ErrorIntegracionDto> errores) {

	}

	protected void log(ActualizacionIntegracionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}

	protected String normalizar(String str, int len) {
		val result = left(defaultString(str), len);
		return result;
	}

}