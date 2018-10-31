package com.egakat.io.gws.cliente.service.impl.solicitudes;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;
import com.egakat.core.io.stage.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesClienteLocalService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoNotificationService;

import lombok.val;

@Service
public class SolicitudesDespachoNotificationServiceImpl implements SolicitudesDespachoNotificationService {

	@Autowired
	private SolicitudesClienteLocalService externalService;

	@Autowired
	private ActualizacionIntegracionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Override
	public void ack(ActualizacionIntegracionDto entry) {
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
	public void reject(ActualizacionIntegracionDto entry) {
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
	public void accept(ActualizacionIntegracionDto entry) {
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
