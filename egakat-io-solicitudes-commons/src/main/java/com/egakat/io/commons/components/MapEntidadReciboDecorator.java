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
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.CANAL_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.CIUDAD_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.CONTACTO_EMAIL;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.CONTACTO_NOMBRES;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.CONTACTO_TELEFONOS;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.DIRECCION;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.DOCUMENTO_CLIENTE;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.FECHA_DOCUMENTO_CLIENTE;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.HOMA;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.HOMI;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.PUNTO_CODIGO_ALTERNO;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.PUNTO_NOMBRE;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.REQUIERE_AGENDAMIENTO;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.REQUIERE_TRANSPORTE;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.TERCERO_IDENTIFICACION;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.TERCERO_NOMBRE;
import static com.egakat.io.commons.solicitudes.domain.SolicitudTercero.VALOR_UNITARIO_DECLARADO;
import static com.egakat.io.commons.solicitudes.domain.recibos.Recibo.BL;
import static com.egakat.io.commons.solicitudes.domain.recibos.Recibo.CONTENEDOR;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;

import com.egakat.integration.commons.archivos.dto.EtlRequestDto;
import com.egakat.integration.commons.archivos.dto.RegistroDto;
import com.egakat.integration.commons.archivos.enums.EstadoRegistroType;
import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.core.files.components.decorators.MapEntidadDecorator;
import com.egakat.io.commons.solicitudes.domain.recibos.Recibo;

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

		val result = new Recibo();
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

		result.setDocumentoCliente(StringUtils.defaultString(getString(archivo, registro, DOCUMENTO_CLIENTE)));
		result.setFechaDocumentoCliente(fechaDocumentoCliente);

		result.setTerceroIdentificacion(
				StringUtils.defaultString(getString(archivo, registro, TERCERO_IDENTIFICACION)));
		result.setTerceroNombre(StringUtils.defaultString(getString(archivo, registro, TERCERO_NOMBRE)));
		result.setCanalCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, CANAL_CODIGO_ALTERNO)));

		result.setRequiereTransporte(StringUtils.defaultString(getString(archivo, registro, REQUIERE_TRANSPORTE)));
		result.setCiudadCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, CIUDAD_CODIGO_ALTERNO)));
		result.setDireccion(StringUtils.defaultString(getString(archivo, registro, DIRECCION)));
		result.setPuntoCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, PUNTO_CODIGO_ALTERNO)));
		result.setPuntoNombre(StringUtils.defaultString(getString(archivo, registro, PUNTO_NOMBRE)));

		result.setRequiereAgendamiento(StringUtils.defaultString(getString(archivo, registro, REQUIERE_AGENDAMIENTO)));
		result.setFemi(femi);
		result.setFema(fema);
		result.setHomi(homi);
		result.setHoma(homa);

		result.setContactoNombres(StringUtils.defaultString(getString(archivo, registro, CONTACTO_NOMBRES)));
		result.setContactoTelefonos(StringUtils.defaultString(getString(archivo, registro, CONTACTO_TELEFONOS)));
		result.setContactoEmail(StringUtils.defaultString(getString(archivo, registro, CONTACTO_EMAIL)));
		result.setNota(StringUtils.defaultString(getString(archivo, registro, NOTA)));

		result.setProductoCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, PRODUCTO_CODIGO_ALTERNO)));
		result.setProductoNombre(StringUtils.defaultString(getString(archivo, registro, PRODUCTO_NOMBRE)));
		result.setUnidadMedidaCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, UNIDAD_MEDIDA_CODIGO_ALTERNO)));
		result.setCantidad(cantidad);
		result.setBodegaCodigoAlterno(StringUtils.defaultString(getString(archivo, registro, BODEGA_CODIGO_ALTERNO)));
		result.setEstadoInventarioCodigoAlterno(
				StringUtils.defaultString(getString(archivo, registro, ESTADO_INVENTARIO_CODIGO_ALTERNO)));

		result.setLote(StringUtils.defaultString(getString(archivo, registro, LOTE)));
		result.setBl(StringUtils.defaultString(getString(archivo, registro, BL)));
		result.setContenedor(StringUtils.defaultString(getString(archivo, registro, CONTENEDOR)));

		result.setValorUnitarioDeclarado(valorUnitarioDeclarado);

		return result;
	}
}