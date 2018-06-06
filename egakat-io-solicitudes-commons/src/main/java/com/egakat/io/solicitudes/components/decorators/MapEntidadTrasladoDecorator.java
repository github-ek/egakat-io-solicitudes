package com.egakat.io.solicitudes.components.decorators;

import static com.egakat.io.solicitudes.domain.Solicitud.BODEGA_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.CANTIDAD;
import static com.egakat.io.solicitudes.domain.Solicitud.CLIENTE_CODIGO;
import static com.egakat.io.solicitudes.domain.Solicitud.ESTADO_INVENTARIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.FEMA;
import static com.egakat.io.solicitudes.domain.Solicitud.FEMI;
import static com.egakat.io.solicitudes.domain.Solicitud.NOTA;
import static com.egakat.io.solicitudes.domain.Solicitud.NUMERO_SOLICITUD;
import static com.egakat.io.solicitudes.domain.Solicitud.NUMERO_SOLICITUD_SIN_PREFIJO;
import static com.egakat.io.solicitudes.domain.Solicitud.PREFIJO;
import static com.egakat.io.solicitudes.domain.Solicitud.PRODUCTO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.PRODUCTO_NOMBRE;
import static com.egakat.io.solicitudes.domain.Solicitud.SERVICIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.UNIDAD_MEDIDA_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.salidas.Traslado.BODEGA_TRASLADO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.salidas.Traslado.ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.salidas.Traslado.VALOR_UNITARIO_DECLARADO;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.MapEntidadDecorator;
import com.egakat.integration.files.dto.EtlRequestDto;
import com.egakat.integration.files.dto.RegistroDto;
import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.Solicitud;
import com.egakat.io.solicitudes.domain.salidas.Traslado;

import lombok.val;

public class MapEntidadTrasladoDecorator extends MapEntidadDecorator<Traslado, Long> {

	public MapEntidadTrasladoDecorator(Decorator<Traslado, Long> inner) {
		super(inner);
	}

	@Override
	protected Traslado map(EtlRequestDto<Traslado, Long> archivo, RegistroDto<Traslado, Long> registro) {

		LocalDate femi = getLocalDate(archivo, registro, FEMI);
		LocalDate fema = getLocalDate(archivo, registro, FEMA);
		Integer cantidad = getInteger(archivo, registro, CANTIDAD);
		Long valorUnitarioDeclarado = getLong(archivo, registro, VALOR_UNITARIO_DECLARADO);

		// @formatter:off
		val result = 
				Traslado.builder()
				.idArchivo(archivo.getArchivo().getId())
				.estado(EstadoRegistroType.ESTRUCTURA_VALIDA)
				.numeroLinea(registro.getNumeroLinea())
				.version(0)
				
				.clienteCodigo(StringUtils.defaultString(getString(archivo, registro,CLIENTE_CODIGO)))
				.servicioCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,SERVICIO_CODIGO_ALTERNO)))
				.numeroSolicitud(StringUtils.defaultString(getString(archivo, registro,NUMERO_SOLICITUD)))
				.prefijo(StringUtils.defaultString(getString(archivo, registro,PREFIJO)))
				.numeroSolicitudSinPrefijo(StringUtils.defaultString(getString(archivo, registro,NUMERO_SOLICITUD_SIN_PREFIJO)))

				.femi(femi)
				.fema(fema)
				.nota(StringUtils.defaultString(getString(archivo, registro,NOTA)))

				.productoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,PRODUCTO_CODIGO_ALTERNO)))
				.productoNombre(StringUtils.defaultString(getString(archivo, registro,PRODUCTO_NOMBRE)))
				.cantidad(cantidad)
				.unidadMedidaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,UNIDAD_MEDIDA_CODIGO_ALTERNO)))
				.bodegaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,BODEGA_CODIGO_ALTERNO)))
				.estadoInventarioCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,ESTADO_INVENTARIO_CODIGO_ALTERNO)))

				.bodegaTrasladoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, BODEGA_TRASLADO_CODIGO_ALTERNO)))
				.estadoInventarioTrasladoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO)))
				
				.lote(StringUtils.defaultString(getString(archivo, registro,Solicitud.LOTE)))
				.valorUnitarioDeclarado(valorUnitarioDeclarado)
				
				.build();
		// @formatter:on

		return result;
	}
}