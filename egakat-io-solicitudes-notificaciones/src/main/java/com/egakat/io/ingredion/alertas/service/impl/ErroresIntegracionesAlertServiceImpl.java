package com.egakat.io.ingredion.alertas.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;
import com.egakat.io.ingredion.alertas.service.api.ErroresIntegracionesAlertService;

import lombok.val;

@Service
public class ErroresIntegracionesAlertServiceImpl extends AbstractErroresIntegracionesAlertServiceImpl
		implements ErroresIntegracionesAlertService {

	protected static final String ALERT_CODE = "ERRORES INTEGRACION ACTAS INGREDION - REMESAS SILOGTRAN";

	@Override
	protected String getCode() {
		return ALERT_CODE;
	}

	@Override
	protected String getContentPathResource() {
		return "templates\\errores.html";
	}

	@Override
	protected Map<String, Object> getData(List<ErrorActaDto> request) {
		val result = super.getData(request);
		
		result.put(PARAM_TITLE, "Reporte de errores ocurridos durante el envío de las actas de INGREDION a SILOGTRAN");
		return result;
	}
}
