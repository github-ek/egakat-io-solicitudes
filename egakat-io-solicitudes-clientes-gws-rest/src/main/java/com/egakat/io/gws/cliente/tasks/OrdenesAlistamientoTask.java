package com.egakat.io.gws.cliente.tasks;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.egakat.io.gws.cliente.service.api.ordenes.OrdenesAlistamientoDownloadService;
import com.egakat.io.gws.cliente.service.api.ordenes.OrdenesAlistamientoPullService;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.gws.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrdenesAlistamientoTask {

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
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private OrdenAlistamientoCrudService crudService;

	private void push() {
		val actualizaciones = actualizacionesService.findAllByEstadoIntegracionIn(
				IntegracionesConstants.ORDENES_DE_SALIDA_EN_STAGE, EstadoIntegracionType.ESTRUCTURA_VALIDA);

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

	private void push(ActualizacionIntegracionDto actualizacion, ArrayList<ErrorIntegracionDto> errores) {
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

	private OrdenAlistamientoDto getInput(ActualizacionIntegracionDto actualizacion,
			ArrayList<ErrorIntegracionDto> errores) {
		val result = crudService.findOneByIntegracionAndIdExterno(actualizacion.getIntegracion(),
				actualizacion.getIdExterno());
		return result;
	}
}
