package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto.error;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.service.api.NotificationSchedulerService;
import com.egakat.io.solicitudes.gws.service.api.client.SalidasLocalService;
import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;

@Service
public class NotificationSchedulerServiceImpl implements NotificationSchedulerService {

	@Autowired
	private SalidasLocalService externalService;

	@Autowired
	private EntradaIntegracionCrudService entradasService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Override
	public void ack(EntradaIntegracionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			externalService.confirmarRecibo(id);
		} catch (RuntimeException e) {
			val error = error(entry.getIntegracion(), entry.getIdExterno(), "", e);
			errores.add(error);
		}

		if (errores.isEmpty()) {
			entry.setProgramarNotificacion(false);
			entry.setNotificacionRealizada(true);
			// TODO programar salidas
		} else {
			// TODO guardar errores de intentos
		}
		entradasService.update(entry);
	}

	@Override
	public void reject(EntradaIntegracionDto entry) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			val id = Integer.parseInt(entry.getIdExterno());
			val list = erroresService.findAllByIntegracionAndIdExterno(entry.getIntegracion(), entry.getIdExterno());
			externalService.notificarErrores(id, list);
		} catch (RuntimeException e) {
			val error = error(entry.getIntegracion(), entry.getIdExterno(), "", e);
			errores.add(error);
			e.printStackTrace();
		}

		if (errores.isEmpty()) {
			entry.setProgramarNotificacion(false);
			entry.setNotificacionRealizada(true);
			// TODO programar salidas
		} else {
			// TODO guardar errores de intentos
		}
		entradasService.update(entry);
	}

	@Override
	public void accept(EntradaIntegracionDto entry) {
		// TODO Auto-generated method stub

	}

}
