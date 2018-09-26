package com.egakat.io.gws.cliente.service.impl.documentos;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.cliente.service.api.documentos.DocumentoSolicitudNotificationService;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesClienteLocalService;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;
import com.egakat.io.gws.commons.core.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;

@Service
public class DocumentoSolicitudNotificationServiceImpl implements DocumentoSolicitudNotificationService {

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
			externalService.confirmarReciboDocumento(id);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		entry.setEstadoIntegracion(EstadoIntegracionType.PROCESADO);
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

//		try {
//			val id = Integer.parseInt(entry.getIdExterno());
//			externalService.aceptar(id);
//		} catch (RuntimeException e) {
//			val error = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "", e);
//			errores.add(error);
//		}

		actualizacionesService.updateEstadoNotificacion(entry, errores, EstadoNotificacionType.NOTIFICADA,
				EstadoNotificacionType.ERROR);
	}
}
