package com.egakat.core.io.stage.service.impl;

import static com.egakat.core.io.stage.enums.EstadoIntegracionType.ERROR_HOMOLOGACION;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.service.api.MapService;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ExtendedIntegracionEntityCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class MapServiceImpl<M extends IntegrationEntityDto> implements MapService {

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

	abstract protected String getIntegracion();

	@Override
	public void map() {
		val actualizaciones = getPendientes();

		int i = 1;
		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();
			val model = getModel(actualizacion, errores);

			if (errores.isEmpty()) {
				map(model, actualizacion, errores);
			}

			if (!errores.isEmpty()) {
				onError(model, actualizacion, errores);
				try {
					getCrudService().update(model, actualizacion, ERROR_HOMOLOGACION, errores);
				} catch (RuntimeException e) {
					log.error("Exception:", e);
				}
			}

			i++;
		}
	}

	abstract protected List<ActualizacionIntegracionDto> getPendientes();

	protected void map(M model, ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		try {
			map(model, errores);
			validate(model, errores);

			if (errores.isEmpty()) {
				onSuccess(model, actualizacion);
				getCrudService().update(model, actualizacion, actualizacion.getEstadoIntegracion());
			}
		} catch (RuntimeException e) {
			val error = getErroresService().error(actualizacion, "", e);
			errores.add(error);
			log.error("Exception:", e);
		}
	}

	protected M getModel(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val result = getCrudService().findOneByIntegracionAndIdExterno(actualizacion.getIntegracion(),
				actualizacion.getIdExterno());
		return result;
	}

	abstract protected void map(M model, List<ErrorIntegracionDto> errores);

	abstract protected void validate(M model, List<ErrorIntegracionDto> errores);

	abstract protected void onSuccess(M model, ActualizacionIntegracionDto actualizacion);

	abstract protected void onError(M model, ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores);

	protected void log(ActualizacionIntegracionDto a, int i, int n) {
		val format = "integracion={}, correlacion={}, id externo={}: {} de {}.";
		log.debug(format, a.getIntegracion(), a.getCorrelacion(), a.getIdExterno(), i, n);
	}
}