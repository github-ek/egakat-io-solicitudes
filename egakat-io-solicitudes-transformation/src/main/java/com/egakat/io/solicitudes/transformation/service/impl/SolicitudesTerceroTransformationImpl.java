package com.egakat.io.solicitudes.transformation.service.impl;

import static com.egakat.io.solicitudes.domain.SolicitudTercero.CANAL_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CIUDAD_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.PUNTO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.TERCERO_IDENTIFICACION;

import com.egakat.integration.files.dto.CampoDto;
import com.egakat.io.solicitudes.domain.SolicitudTercero;

import lombok.val;

public abstract class SolicitudesTerceroTransformationImpl<T extends SolicitudTercero> extends SolicitudesTransformationServiceImpl<T> {

	public SolicitudesTerceroTransformationImpl() {
		super();
	}

	@Override
	protected void translateField(T registro, CampoDto campo, String value) {
		switch (campo.getCodigo()) {
		case TERCERO_IDENTIFICACION:
			translateTercero(registro, value);
			break;
		case CANAL_CODIGO_ALTERNO:
			translateCanal(registro, value);
			break;
		case CIUDAD_CODIGO_ALTERNO:
			translateCiudad(registro, value);
			break;
		case PUNTO_CODIGO_ALTERNO:
			translatePunto(registro, value);
			break;
		default:
			super.translateField(registro, campo, value);
		}
	}

	private void translateTercero(T registro, String value) {
		registro.setIdTercero(null);
		val cliente = registro.getIdCliente();
		if (cliente != null) {
			val id = getLookUpService().findTerceroIdByIdAndNumeroIdentificacion(cliente.longValue(), value);
			registro.setIdTercero(id);
		}
	}

	private void translateCanal(T registro, String value) {
		registro.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(value);
		registro.setIdCanal(id);
	}

	private void translateCiudad(T registro, String value) {
		registro.setIdCiudad(null);
		Long id;
		id = getLookUpService().findCiudadIdByNombreAlterno(value);
		if(id == null) {
			id = getLookUpService().findCiudadIdByCodigo(value);
		}
		registro.setIdCiudad(id);
	}

	private void translatePunto(T registro, String value) {
		registro.setIdPunto(null);
		val tercero = registro.getIdTercero();
		if (tercero != null) {
			val id = getLookUpService().findPuntoIdByTerceroIdAndPuntoCodigo(tercero.longValue(), value);
			registro.setIdPunto(id);
		}
	}
}