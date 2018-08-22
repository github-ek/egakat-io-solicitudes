package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto.error;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoNotificacionType;
import com.egakat.io.solicitudes.gws.service.api.NotificationService;
import com.egakat.io.solicitudes.gws.service.api.client.SalidasLocalService;
import com.egakat.io.solicitudes.gws.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private SalidasLocalService externalService;

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
			val error = error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "", e);
			errores.add(error);
		}

		actualizacionesService.update(entry, errores, EstadoNotificacionType.NOTIFICADA, EstadoNotificacionType.ERROR);
	}

	@Override
	public void reject(ActualizacionIntegracionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			val list = erroresService.findAllByIntegracionAndIdExternoAndCorrelacion(entry.getIntegracion(),
					entry.getIdExterno(), entry.getCorrelacion());
			externalService.rechazar(id, list);
		} catch (RuntimeException e) {
			val error = error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "", e);
			errores.add(error);
		}

		actualizacionesService.update(entry, errores, EstadoNotificacionType.NOTIFICADA, EstadoNotificacionType.ERROR);
	}

	@Override
	public void accept(ActualizacionIntegracionDto entry) {
		// TODO Auto-generated method stub

	}
}
