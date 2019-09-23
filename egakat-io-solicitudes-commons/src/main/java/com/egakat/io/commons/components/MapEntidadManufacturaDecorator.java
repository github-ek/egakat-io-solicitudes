package com.egakat.io.commons.components;

import static com.egakat.io.commons.solicitudes.domain.Solicitud.BODEGA_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.CANTIDAD;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.CLIENTE_CODIGO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.ESTADO_INVENTARIO_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.FEMA;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.FEMI;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.LOTE;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.NOTA;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.NUMERO_SOLICITUD;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.NUMERO_SOLICITUD_SIN_PREFIJO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.PREFIJO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.PRODUCTO_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.PRODUCTO_NOMBRE;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.SERVICIO_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.Solicitud.UNIDAD_MEDIDA_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura.REQUIERE_BOM;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import com.egakat.integration.commons.archivos.dto.EtlRequestDto;
import com.egakat.integration.commons.archivos.dto.RegistroDto;
import com.egakat.integration.commons.archivos.enums.EstadoRegistroType;
import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.MapEntidadDecorator;
import com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura;

import lombok.val;

public class MapEntidadManufacturaDecorator extends MapEntidadDecorator<Manufactura, Long> {

	public MapEntidadManufacturaDecorator(Decorator<Manufactura, Long> inner) {
		super(inner);
	}

	@Override
	protected Manufactura map(EtlRequestDto<Manufactura, Long> archivo, RegistroDto<Manufactura, Long> registro) {
		LocalDate femi = getLocalDate(archivo, registro, FEMI);
		LocalDate fema = getLocalDate(archivo, registro, FEMA);
		Integer cantidad = getInteger(archivo, registro, CANTIDAD);
		Integer requiereBom = getInteger(archivo, registro, REQUIERE_BOM);

		// @formatter:off
 		val result = new Manufactura();
		result.setIdArchivo(archivo.getArchivo().getId());
		result.setEstado(EstadoRegistroType.ESTRUCTURA_VALIDA);
		result.setNumeroLinea(registro.getNumeroLinea());
		result.setVersion(0);

		result.setClienteCodigo(StringUtils.defaultString(getString(archivo, registro, CLIENTE_CODIGO)));
		result.setServicioCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, SERVICIO_CODIGO_ALTERNO)));
		result.setNumeroSolicitud(StringUtils.defaultString(getString(archivo, registro, NUMERO_SOLICITUD)));
		result.setPrefijo(StringUtils.defaultString(getString(archivo, registro, PREFIJO)));
		result.setNumeroSolicitudSinPrefijo(
				StringUtils.defaultString(getString(archivo, registro, NUMERO_SOLICITUD_SIN_PREFIJO)));

		result.setFemi(femi);
		result.setFema(fema);
		result.setNota(StringUtils.defaultString(getString(archivo, registro, NOTA)));

		result.setProductoCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, PRODUCTO_CODIGO_ALTERNO)));
		result.setProductoNombre(StringUtils.defaultString(getString(archivo, registro, PRODUCTO_NOMBRE)));
		result.setCantidad(cantidad);
		result.setUnidadMedidaCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, UNIDAD_MEDIDA_CODIGO_ALTERNO)));
		result.setBodegaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, BODEGA_CODIGO_ALTERNO)));
		result.setEstadoInventarioCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, ESTADO_INVENTARIO_CODIGO_ALTERNO)));

		result.setLote(StringUtils.defaultString(getString(archivo, registro, LOTE)));
				
		result.setRequiereBom(requiereBom.intValue() != 0);

		return result;
	}
}