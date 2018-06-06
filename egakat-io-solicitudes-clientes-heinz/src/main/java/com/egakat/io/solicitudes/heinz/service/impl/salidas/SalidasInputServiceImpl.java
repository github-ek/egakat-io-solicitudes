package com.egakat.io.solicitudes.heinz.service.impl.salidas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.readers.ExcelWorkSheetReader;
import com.egakat.integration.core.files.components.readers.Reader;
import com.egakat.integration.core.files.service.impl.excel.ArchivoExcelInputServiceImpl;
import com.egakat.io.solicitudes.components.decorators.MapEntidadSalidaDecorator;
import com.egakat.io.solicitudes.domain.salidas.Salida;
import com.egakat.io.solicitudes.heinz.service.api.SalidasInputService;
import com.egakat.io.solicitudes.repository.salidas.SalidaRepository;

import lombok.val;

@Service
public class SalidasInputServiceImpl extends ArchivoExcelInputServiceImpl<Salida> implements SalidasInputService{

	private static final String TIPO_ARCHIVO_CODIGO = "HEINZ_SALIDAS";

	private static final String WORKSHEET_NAME = "0";

	@Autowired
	private SalidaRepository repository;

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}
	
	@Override
	protected String getWorkSheetName() {
		return WORKSHEET_NAME;
	}
	
	@Override
	protected Reader getReader() {
		val reader = (ExcelWorkSheetReader) super.getReader();
		reader.setRowOffset(6);
		return reader;
	}

	@Override
	protected SalidaRepository getRepository() {
		return repository;
	}
	
	@Override
	protected Decorator<Salida, Long> getMapEntidadDecorator(Decorator<Salida, Long> inner) {
		return new MapEntidadSalidaDecorator(inner);
	}
}