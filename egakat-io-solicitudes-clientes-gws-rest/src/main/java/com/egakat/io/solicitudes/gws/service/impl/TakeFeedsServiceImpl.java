package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.configuration.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.service.api.TakeFeedsService;
import com.egakat.io.solicitudes.gws.service.api.cliente.solicitudes.SolicitudClienteLocalService;
import com.egakat.io.solicitudes.gws.service.api.crud.ActualizacionIntegracionCrudService;

import lombok.val;

@Service
public class TakeFeedsServiceImpl implements TakeFeedsService {

	@Autowired
	private SolicitudClienteLocalService localService;

	@Autowired
	private ActualizacionIntegracionCrudService entradasService;

	@Override
	public String getCodigoIntegracion() {
		return SOLICITUDES_SALIDAS;
	}

	@Override
	public void takeFeeds() {
		val ids = localService.getAllByStatus("ENVIAR");
		val correlacion = LocalDateTime.now().toString();
		for (val id : ids) {
			val model = asEntradaIntegracion(id, correlacion);

			entradasService.enqueue(model);
		}

	}

	protected ActualizacionIntegracionDto asEntradaIntegracion(Integer id, String correlacion) {
		// @formatter:off
		val result = ActualizacionIntegracionDto
				.builder()
				.integracion(getCodigoIntegracion())
				.idExterno(id.toString())
				.correlacion(correlacion)
				.estadoExterno("")
				.estadoIntegracion(EstadoIntegracionType.NO_PROCESADO)
				.build();				
		// @formatter:on

		return result;
	}

}
