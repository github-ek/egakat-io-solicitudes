package com.egakat.io.solicitudes.components.decorators;

import static com.egakat.io.solicitudes.domain.Solicitud.BODEGA_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.CANTIDAD;
import static com.egakat.io.solicitudes.domain.Solicitud.CLIENTE_CODIGO;
import static com.egakat.io.solicitudes.domain.Solicitud.ESTADO_INVENTARIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.FEMA;
import static com.egakat.io.solicitudes.domain.Solicitud.FEMI;
import static com.egakat.io.solicitudes.domain.Solicitud.LOTE;
import static com.egakat.io.solicitudes.domain.Solicitud.NOTA;
import static com.egakat.io.solicitudes.domain.Solicitud.NUMERO_SOLICITUD;
import static com.egakat.io.solicitudes.domain.Solicitud.NUMERO_SOLICITUD_SIN_PREFIJO;
import static com.egakat.io.solicitudes.domain.Solicitud.PREFIJO;
import static com.egakat.io.solicitudes.domain.Solicitud.PRODUCTO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.PRODUCTO_NOMBRE;
import static com.egakat.io.solicitudes.domain.Solicitud.SERVICIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.UNIDAD_MEDIDA_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.CANTIDAD_BOM;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.PRODUCTO_BOM_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom.SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDate;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.MapEntidadDecorator;
import com.egakat.integration.files.dto.EtlRequestDto;
import com.egakat.integration.files.dto.RegistroDto;
import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.manufacturas.ManufacturaBom;

import lombok.val;

public class MapEntidadManufacturaBomDecorator extends MapEntidadDecorator<ManufacturaBom, Long> {

	public MapEntidadManufacturaBomDecorator(Decorator<ManufacturaBom, Long> inner) {
		super(inner);
	}

	@Override
	protected ManufacturaBom map(EtlRequestDto<ManufacturaBom, Long> archivo,
			RegistroDto<ManufacturaBom, Long> registro) {
		LocalDate femi = getLocalDate(archivo, registro, FEMI);
		LocalDate fema = getLocalDate(archivo, registro, FEMA);
		Integer cantidad = getInteger(archivo, registro, CANTIDAD);
		Integer cantidadBom = getInteger(archivo, registro, CANTIDAD_BOM);

		// @formatter:off
 		val result = ManufacturaBom.builder()
				.idArchivo(archivo.getArchivo().getId())
				.estado(EstadoRegistroType.ESTRUCTURA_VALIDA)
				.numeroLinea(registro.getNumeroLinea())
				.version(0)
				
				.clienteCodigo(defaultString(getString(archivo, registro,CLIENTE_CODIGO)))
				.servicioCodigoAlterno(defaultString(getString(archivo, registro,SERVICIO_CODIGO_ALTERNO)))
				.numeroSolicitud(defaultString(getString(archivo, registro,NUMERO_SOLICITUD)))
				.prefijo(defaultString(getString(archivo, registro,PREFIJO)))
				.numeroSolicitudSinPrefijo(defaultString(getString(archivo, registro,NUMERO_SOLICITUD_SIN_PREFIJO)))

				.femi(femi)
				.fema(fema)
				.nota(defaultString(getString(archivo, registro,NOTA)))
				
				.productoCodigoAlterno(defaultString(getString(archivo, registro,PRODUCTO_CODIGO_ALTERNO)))
				.productoNombre(defaultString(getString(archivo, registro,PRODUCTO_NOMBRE)))
				.cantidad(cantidad)
				.unidadMedidaCodigoAlterno(defaultString(getString(archivo, registro,UNIDAD_MEDIDA_CODIGO_ALTERNO)))
				.bodegaCodigoAlterno(defaultString(getString(archivo, registro,BODEGA_CODIGO_ALTERNO)))
				.estadoInventarioCodigoAlterno(defaultString(getString(archivo, registro,ESTADO_INVENTARIO_CODIGO_ALTERNO)))
				
				.lote(defaultString(getString(archivo, registro,LOTE)))
				
				.productoBomCodigoAlterno(defaultString(getString(archivo, registro,PRODUCTO_BOM_CODIGO_ALTERNO)))
				.cantidadBom(cantidadBom)
				.estadoInventarioBomCodigoAlterno(defaultString(getString(archivo, registro,ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO)))
				.subestadoInventarioBomCodigoAlterno(defaultString(getString(archivo, registro,SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO)))
				
				.build();
		// @formatter:on

		return result;
	}
}