package com.egakat.io.solicitudes.gws.service.impl.traslados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.IncluirEncabezadoDecorator;
import com.egakat.integration.core.files.service.impl.flat.ArchivoPlanoInputServiceImpl;
import com.egakat.io.solicitudes.components.decorators.MapEntidadTrasladoDecorator;
import com.egakat.io.solicitudes.domain.salidas.Traslado;
import com.egakat.io.solicitudes.gws.components.decorators.traslados.AbastecimientosCamposDecorator;
import com.egakat.io.solicitudes.gws.service.api.AbastecimientosInputService;
import com.egakat.io.solicitudes.repository.salidas.TrasladoRepository;

@Service
public class AbastecimientosInputServiceImpl extends ArchivoPlanoInputServiceImpl<Traslado> implements AbastecimientosInputService {

	private static final String TIPO_ARCHIVO_CODIGO = "GWS_ABASTECIMIENTOS";

	@Autowired
	private TrasladoRepository repository;

	@Override
	public String getTipoArchivoCodigo() {
		return TIPO_ARCHIVO_CODIGO;
	}

	@Override
	protected TrasladoRepository getRepository() {
		return repository;
	}

	@Override
	protected Decorator<Traslado, Long> getIncluirEncabezadoDecorator(Decorator<Traslado, Long> inner) {
		return new IncluirEncabezadoDecorator<>(inner);
	}

	@Override
	protected Decorator<Traslado, Long> getEnriquecerCamposDecorator(Decorator<Traslado, Long> inner) {
		return new AbastecimientosCamposDecorator(inner);
	}

	@Override
	protected Decorator<Traslado, Long> getMapEntidadDecorator(Decorator<Traslado, Long> inner) {
		return new MapEntidadTrasladoDecorator(inner);
	}
}