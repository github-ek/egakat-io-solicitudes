package com.egakat.io.gws.service.impl.documentos;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.api.crud.ActualizacionCrudService;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudNotificationService;

import lombok.val;

@Service
public class DocumentoSolicitudNotificationServiceImpl implements DocumentoSolicitudNotificationService {

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Override
	public void ack(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			//externalService.confirmarReciboDocumento(id);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		entry.setEstadoIntegracion(EstadoIntegracionType.PROCESADO);
		entry.setEstadoNotificacion(EstadoNotificacionType.NOTIFICADA);
		actualizacionesService.updateNotificacion(entry, errores);
	}

	@Override
	public void reject(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			val list = erroresService.findAll(entry);
			//externalService.rechazar(id, list);
		} catch (RuntimeException e) {
			val error = erroresService.error(entry, "", e);
			errores.add(error);
		}

		entry.setEstadoNotificacion(EstadoNotificacionType.NOTIFICADA);
		actualizacionesService.updateNotificacion(entry, errores);
	}

	@Override
	public void accept(ActualizacionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

//		try {
//			val id = Integer.parseInt(entry.getIdExterno());
//			externalService.aceptar(id);
//		} catch (RuntimeException e) {
//			val error = erroresService.error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "", e);
//			errores.add(error);
//		}

		actualizacionesService.updateNotificacion(entry, errores);
	}
}
