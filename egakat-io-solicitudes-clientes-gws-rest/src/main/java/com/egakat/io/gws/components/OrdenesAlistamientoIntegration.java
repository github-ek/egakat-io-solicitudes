package com.egakat.io.gws.components;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.egakat.io.commons.configuration.constants.IntegracionesConstants;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoDownloadService;
import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrdenesAlistamientoIntegration {

	@Autowired
	private OrdenesAlistamientoPullService pullService;

	@Autowired
	private OrdenesAlistamientoDownloadService downloadService;

	// @Autowired
	// private OrdenesAlistamientoNotificationService notificationService;

	// @Autowired
	// private SolicitudDespachoDataQualityService dataQualityService;

	public void run() {
		pull();
		download();
		ack();
		validate();
		push();
	}

	protected void pull() {
		try {
			pullService.pull();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void download() {
		try {
			downloadService.download();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void validate() {
		try {

		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void ack() {
		// val entries =
		// actualizacionesService.findAllNoNotificadasByEstadoIntegracionIn(
		// IntegracionesConstants.SOLICITUDES_SALIDAS, ESTRUCTURA_VALIDA);
		//
		// for (val entry : entries) {
		// try {
		// notificationService.ack(entry);
		// } catch (RuntimeException e) {
		// log.error("", e);
		// }
		// }
	}

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private OrdenAlistamientoCrudService crudService;

	private void push() {
		val actualizaciones = actualizacionesService.findAllByIntegracionAndEstadoIntegracionIn(
				IntegracionesConstants.ORDENES_DE_ALISTAMIENTO_EN_STAGE, EstadoIntegracionType.ESTRUCTURA_VALIDA);

//		int i = 1;
//		int n = actualizaciones.size();
		for (val actualizacion : actualizaciones) {
			// log(actualizacion, i, n);

			val errores = new ArrayList<ErrorIntegracionDto>();

			push(actualizacion, errores);

			if (!errores.isEmpty()) {
				// onError(actualizacion, errores);
				try {
					actualizacionesService.update(actualizacion, EstadoIntegracionType.ERROR_CARGUE, errores);
				} catch (Exception e) {
					log.error("Exception:", e);
				}
			}

			//i++;
		}

	}

	private void push(ActualizacionDto actualizacion, ArrayList<ErrorIntegracionDto> errores) {
		try {
			val input = getInput(actualizacion, errores);

			if (errores.isEmpty()) {
				// validate(actualizacion, input, errores);

				if (errores.isEmpty()) {
					Long id = crudService.upload(input, errores);
					// val model = asModel(actualizacion, input);
					// onSuccess(actualizacion, input, model);
					if (errores.isEmpty()) {
						crudService.updateEstadoNoficacionOrdenAlistamiento(input, actualizacion,
								EstadoIntegracionType.PROCESADO, id);
					}
				}
			}
		} catch (RuntimeException e) {
			val error = erroresService.error(actualizacion, "", e);
			errores.add(error);
			log.error("Exception:", e);
		}
	}

	private OrdenAlistamientoDto getInput(ActualizacionDto actualizacion,
			ArrayList<ErrorIntegracionDto> errores) {
		val result = crudService.findOneByIntegracionAndIdExterno(actualizacion.getIntegracion(),
				actualizacion.getIdExterno());
		return result;
	}
}
