package com.egakat.io.gws.service.impl.solicitudes;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.client.service.api.SolicitudesClienteLocalService;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoNotificationService;

import lombok.val;

@Service
public class SolicitudesDespachoNotificationServiceImpl implements SolicitudesDespachoNotificationService {

	@Autowired
	private SolicitudesClienteLocalService externalService;

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Override
	public void ack(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			externalService.confirmarRecibo(id);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		actualizacionesService.updateEstadoNotificacion(entry, errores, EstadoNotificacionType.NOTIFICADA,
				EstadoNotificacionType.ERROR);
	}

	@Override
	public void reject(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			val list = erroresService.findAll(entry);
			externalService.rechazar(id, list);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		actualizacionesService.updateEstadoNotificacion(entry, errores, EstadoNotificacionType.NOTIFICADA,
				EstadoNotificacionType.ERROR);
	}

	@Override
	public void accept(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			externalService.aceptar(id);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		actualizacionesService.updateEstadoNotificacion(entry, errores, EstadoNotificacionType.NOTIFICADA,
				EstadoNotificacionType.ERROR);
	}
}
