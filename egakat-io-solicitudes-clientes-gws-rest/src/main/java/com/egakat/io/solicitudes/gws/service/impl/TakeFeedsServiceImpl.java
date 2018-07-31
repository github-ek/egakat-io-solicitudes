package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.constants.IntegracionesConstants.SOLICITUDES_SALIDAS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;
import com.egakat.io.solicitudes.gws.service.api.TakeFeedsService;
import com.egakat.io.solicitudes.gws.service.api.client.SalidasLocalService;
import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;

import lombok.val;
@Service
public class TakeFeedsServiceImpl implements TakeFeedsService {

	@Autowired
	private SalidasLocalService localService;

	@Autowired
	private EntradaIntegracionCrudService entradasService;

	@Override
	public String getCodigoIntegracion() {
		return SOLICITUDES_SALIDAS;
	}

	@Override
	public void takeFeeds() {
		val ids = localService.getAllByStatus("MIGRADO");
		for (val id : ids) {
			val model = asEntradaIntegracion(id);
			
			entradasService.enqueue(model);
		}

	}

	protected EntradaIntegracionDto asEntradaIntegracion(Integer id) {
		// @formatter:off
		val result = EntradaIntegracionDto
				.builder()
				.estado(EstadoEntradaIntegracionType.NO_PROCESADO)
				.programarNotificacion(false)
				.notificacionRealizada(false)
				.integracion(getCodigoIntegracion())
				.idExterno(id.toString())
				.estadoExterno("")
				.build();				
		// @formatter:on

		return result;
	}

}
