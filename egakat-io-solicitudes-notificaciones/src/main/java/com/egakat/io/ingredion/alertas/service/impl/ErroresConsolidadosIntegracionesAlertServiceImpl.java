package com.egakat.io.ingredion.alertas.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;
import com.egakat.io.ingredion.alertas.service.api.ErroresConsolidadosIntegracionesAlertService;

import lombok.val;

@Service
public class ErroresConsolidadosIntegracionesAlertServiceImpl extends AbstractErroresIntegracionesAlertServiceImpl
		implements ErroresConsolidadosIntegracionesAlertService {

	protected static final String ALERT_CODE = "ERRORES CONSOLIDADOS INTEGRACION ACTAS INGREDION - REMESAS SILOGTRAN";

	@Override
	protected String getCode() {
		return ALERT_CODE;
	}

	@Override
	protected String getContentPathResource() {
		return "templates\\errores-consolidados.html";
	}

	@Override
	protected Map<String, Object> getData(List<ErrorActaDto> request) {
		val result = super.getData(request);
		
		result.put(PARAM_TITLE, "Reporte CONSOLIDADO de errores ocurridos durante el envío de las actas de INGREDION a SILOGTRAN");
		return result;

	}
}
