package com.egakat.io.solicitudes.transformation.service.impl;

import static com.egakat.io.solicitudes.domain.salidas.Traslado.BODEGA_TRASLADO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.salidas.Traslado.ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.files.dto.ArchivoErrorDto;
import com.egakat.integration.files.dto.CampoDto;
import com.egakat.io.solicitudes.domain.salidas.Traslado;
import com.egakat.io.solicitudes.repository.salidas.TrasladoRepository;
import com.egakat.io.solicitudes.transformation.service.api.TrasladosTransformationService;

import lombok.val;

@Service
public class TrasladosTransformationServiceimpl extends SolicitudesTransformationServiceImpl<Traslado>
		implements TrasladosTransformationService {

	@Autowired
	private TrasladoRepository repository;

	@Override
	protected TrasladoRepository getRepository() {
		return repository;
	}

	@Override
	protected void translateField(Traslado registro, CampoDto campo, String value) {
		switch (campo.getCodigo()) {
		case BODEGA_TRASLADO_CODIGO_ALTERNO:
			translateBodegaTraslado(registro, value);
			break;
		case ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO:
			translateEstadoInventarioTraslado(registro, value);
			break;
		default:
			super.translateField(registro, campo, value);
		}
	}

	private void translateBodegaTraslado(Traslado registro, String value) {
		registro.setIdBodegaTraslado(null);
		val id = getLookUpService().findBodegaIdByCodigo(value);
		registro.setIdBodegaTraslado(id);
	}

	private void translateEstadoInventarioTraslado(Traslado registro, String value) {
		registro.setIdEstadoInventarioTraslado(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(value);
		registro.setIdEstadoInventarioTraslado(id);
	}

	@Override
	protected boolean validateRow(Traslado registro, List<ArchivoErrorDto> errores, List<CampoDto> campos) {
		boolean result = super.validateRow(registro, errores, campos);

		if (registro.getIdBodega() != null && registro.getIdBodega().equals(registro.getIdBodegaTraslado())) {
			String msg = "%s:Los abastecimiento deben realizarse entre diferentes centros de distribución. La bodega de origen (%s) y la de destino (%s), corresponden al mismo CEDI.";
			String bodegaCodigo = registro.getBodegaCodigoAlterno();
			String bodegaCodigoAlterno = registro.getBodegaTrasladoCodigoAlterno();

			msg = String.format(msg, BODEGA_TRASLADO_CODIGO_ALTERNO, bodegaCodigo, bodegaCodigoAlterno);
			val error = ArchivoErrorDto.error(registro.getIdArchivo(), msg, registro.getNumeroLinea(),
					registro.toString());
			errores.add(error);
			result = false;
		}

		return result;
	}

}