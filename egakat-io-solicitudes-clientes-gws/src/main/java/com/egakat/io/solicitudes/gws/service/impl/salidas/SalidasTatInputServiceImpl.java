package com.egakat.io.solicitudes.gws.service.impl.salidas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.IncluirEncabezadoDecorator;
import com.egakat.integration.core.files.service.impl.flat.ArchivoPlanoInputServiceImpl;
import com.egakat.io.solicitudes.components.decorators.MapEntidadSalidaDecorator;
import com.egakat.io.solicitudes.domain.salidas.Salida;
import com.egakat.io.solicitudes.gws.components.decorators.salidas.SalidasCamposDecorator;
import com.egakat.io.solicitudes.gws.service.api.SalidasTatInputService;
import com.egakat.io.solicitudes.repository.salidas.SalidaRepository;

@Service
public class SalidasTatInputServiceImpl extends ArchivoPlanoInputServiceImpl<Salida> implements SalidasTatInputService{

	private static final String TIPO_ARCHIVO_CODIGO = "GWS_TAT";

	@Autowired
	private SalidaRepository repository;

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}

	@Override
	protected SalidaRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Salida, Long> getIncluirEncabezadoDecorator(Decorator<Salida, Long> inner) {
		return new IncluirEncabezadoDecorator<>(inner);
	}

	@Override
	protected Decorator<Salida, Long> getEnriquecerCamposDecorator(Decorator<Salida, Long> inner) {
		return new SalidasCamposDecorator(inner);
	}

	@Override
	protected Decorator<Salida, Long> getMapEntidadDecorator(Decorator<Salida, Long> inner) {
		return new MapEntidadSalidaDecorator(inner);
	}
}