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
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CANAL_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CIUDAD_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CONTACTO_EMAIL;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CONTACTO_NOMBRES;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.CONTACTO_TELEFONOS;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.DIRECCION;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.DOCUMENTO_CLIENTE;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.FECHA_DOCUMENTO_CLIENTE;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.HOMA;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.HOMI;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.PUNTO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.PUNTO_NOMBRE;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.REQUIERE_AGENDAMIENTO;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.REQUIERE_TRANSPORTE;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.TERCERO_IDENTIFICACION;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.TERCERO_NOMBRE;
import static com.egakat.io.solicitudes.domain.SolicitudTercero.VALOR_UNITARIO_DECLARADO;
import static com.egakat.io.solicitudes.domain.recibos.Recibo.BL;
import static com.egakat.io.solicitudes.domain.recibos.Recibo.CONTENEDOR;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.MapEntidadDecorator;
import com.egakat.integration.files.dto.EtlRequestDto;
import com.egakat.integration.files.dto.RegistroDto;
import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.recibos.Recibo;

import lombok.val;

public class MapEntidadReciboDecorator extends MapEntidadDecorator<Recibo, Long> {

	public MapEntidadReciboDecorator(Decorator<Recibo, Long> inner) {
		super(inner);
	}

	@Override
	protected Recibo map(EtlRequestDto<Recibo, Long> archivo, RegistroDto<Recibo, Long> registro) {
		LocalDate femi = getLocalDate(archivo, registro, FEMI);
		LocalDate fema = getLocalDate(archivo, registro, FEMA);
		LocalTime homi = getLocalTime(archivo, registro, HOMI);
		LocalTime homa = getLocalTime(archivo, registro, HOMA);
		Integer cantidad = getInteger(archivo, registro, CANTIDAD);
		Long valorUnitarioDeclarado = getLong(archivo, registro, VALOR_UNITARIO_DECLARADO);
		LocalDate fechaDocumentoCliente = getLocalDate(archivo, registro, FECHA_DOCUMENTO_CLIENTE);

		// @formatter:off
		val result = 
				Recibo.builder()
				.idArchivo(archivo.getArchivo().getId())
				.estado(EstadoRegistroType.ESTRUCTURA_VALIDA)
				.numeroLinea(registro.getNumeroLinea())
				.version(0)

				.clienteCodigo(StringUtils.defaultString(getString(archivo, registro,CLIENTE_CODIGO)))
				.servicioCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,SERVICIO_CODIGO_ALTERNO)))
				.numeroSolicitud(StringUtils.defaultString(getString(archivo, registro,NUMERO_SOLICITUD)))
				.prefijo(StringUtils.defaultString(getString(archivo, registro,PREFIJO)))
				.numeroSolicitudSinPrefijo(StringUtils.defaultString(getString(archivo, registro,NUMERO_SOLICITUD_SIN_PREFIJO)))
				.documentoCliente(StringUtils.defaultString(getString(archivo, registro,DOCUMENTO_CLIENTE)))
				.fechaDocumentoCliente(fechaDocumentoCliente)

				.terceroIdentificacion(StringUtils.defaultString(getString(archivo, registro,TERCERO_IDENTIFICACION)))
				.terceroNombre(StringUtils.defaultString(getString(archivo, registro,TERCERO_NOMBRE)))
				.canalCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,CANAL_CODIGO_ALTERNO)))

				.requiereTransporte(StringUtils.defaultString(getString(archivo, registro,REQUIERE_TRANSPORTE)))
				.ciudadCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,CIUDAD_CODIGO_ALTERNO)))
				.direccion(StringUtils.defaultString(getString(archivo, registro,DIRECCION)))
				.puntoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,PUNTO_CODIGO_ALTERNO)))
				.puntoNombre(StringUtils.defaultString(getString(archivo, registro,PUNTO_NOMBRE)))

				.requiereAgendamiento(StringUtils.defaultString(getString(archivo, registro,REQUIERE_AGENDAMIENTO)))
				.femi(femi)
				.fema(fema)
				.homi(homi)
				.homa(homa)

				.contactoNombres(StringUtils.defaultString(getString(archivo, registro,CONTACTO_NOMBRES)))
				.contactoTelefonos(StringUtils.defaultString(getString(archivo, registro,CONTACTO_TELEFONOS)))
				.contactoEmail(StringUtils.defaultString(getString(archivo, registro,CONTACTO_EMAIL)))
				.nota(StringUtils.defaultString(getString(archivo, registro,NOTA)))

				.productoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,PRODUCTO_CODIGO_ALTERNO)))
				.productoNombre(StringUtils.defaultString(getString(archivo, registro,PRODUCTO_NOMBRE)))
				.unidadMedidaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,UNIDAD_MEDIDA_CODIGO_ALTERNO)))
				.cantidad(cantidad)
				.bodegaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,BODEGA_CODIGO_ALTERNO)))
				.estadoInventarioCodigoAlterno(StringUtils.defaultString(getString(archivo, registro,ESTADO_INVENTARIO_CODIGO_ALTERNO)))

				.lote(StringUtils.defaultString(getString(archivo, registro,LOTE)))
				.bl(StringUtils.defaultString(getString(archivo, registro,BL)))
				.contenedor(StringUtils.defaultString(getString(archivo, registro,CONTENEDOR)))

				.valorUnitarioDeclarado(valorUnitarioDeclarado)
				
				.build();
		// @formatter:on

		return result;
	}
}