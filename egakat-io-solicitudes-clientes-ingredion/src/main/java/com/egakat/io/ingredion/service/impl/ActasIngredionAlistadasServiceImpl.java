package com.egakat.io.ingredion.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.egakat.io.ingredion.dto.ActaDto;
import com.egakat.io.ingredion.dto.ErrorDto;
import com.egakat.io.ingredion.service.api.ActasIngredionAlistadasService;
import com.egakat.io.silogtran.dto.RemesaDto;

import lombok.val;

@Service
public class ActasIngredionAlistadasServiceImpl implements ActasIngredionAlistadasService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<String> getBodegasAlternas() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();

		val sql = "SELECT clave,valor FROM [eIntegration].dbo.mapas_valores WHERE id_mapa = 400 ORDER BY clave";
		val result = jdbcTemplate.query(sql, parameters, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int i) throws SQLException {
				val result = rs.getString("clave");

				return result;
			}
		});
		return result;
	}

	@Override
	public List<ActaDto> getActasAlistadas(LocalDate fechaDesde, LocalDate fechaHasta, List<String> estados,
			List<String> bodegas) {

		val sb = new StringBuilder();
		sb.append(getSqlSelect());

		val desde = LocalDateTime.of(fechaDesde, LocalTime.MIDNIGHT);
		val hasta = LocalDateTime.of(fechaHasta, LocalTime.MIDNIGHT).plusDays(1);

		if (estados.isEmpty()) {
			estados.add("ORDEN_ALISTAMIENTO_EN_STAGE");
		}

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("desde", desde);
		parameters.addValue("hasta", hasta);
		parameters.addValue("estados", estados);

		if (!bodegas.isEmpty()) {
			parameters.addValue("bodegas", bodegas);
			sb.append("AND a.bodega_codigo_alterno IN (:bodegas)\r\n");
		}

		val sql = sb.toString();
		val result = jdbcTemplate.query(sql, parameters, getRowMapper());
		return result;
	}

	private String getSqlSelect() {
		// @formatter:off
		val result = 
				"SELECT ROW_NUMBER() OVER(ORDER BY bodega_codigo_alterno,solicitud_numero_documento) AS [rowNumber], a.* \r\n" + 
				"FROM dbo.ActasIngredionAlistadas() a \r\n" + 
				"WHERE \r\n" + 
				"    a.stgdte >= :desde\r\n" + 
				"AND a.stgdte <= :hasta\r\n" +
				"AND a.estado_solicitud IN (:estados)\r\n" +
				"";
		// @formatter:on
		return result;
	}

	private RowMapper<ActaDto> getRowMapper() {
		return new RowMapper<ActaDto>() {
			@Override
			public ActaDto mapRow(ResultSet rs, int i) throws SQLException {
				val result = new ActaDto();

				result.setIdExterno(rs.getString("id_externo"));
				result.setEstadoIntegracion(rs.getString("estado_solicitud"));
				result.setSubEstadoIntegracion(rs.getString("subestado_solicitud"));
				result.setReintentos(rs.getInt("reintentos"));
				result.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
				result.setFechaModificacion(rs.getTimestamp("fecha_modificacion").toLocalDateTime());
				
				
				result.setRegistro(rs.getString("registro"));
				result.setRowNumber(rs.getLong("rowNumber"));
				result.setIdSolicitudActa(rs.getLong("id_solicitud_acta"));
				result.setEstadoSolicitud(rs.getString("estado_solicitud"));
				result.setIdBodega(rs.getLong("id_bodega"));
				result.setIdBodegaVirtual(rs.getLong("id_bodega_virtual"));
				result.setFechaMinimaSolicitada(rs.getDate("fecha_minima_solicitada").toLocalDate());
				result.setFechaMaximaSolicitada(rs.getDate("fecha_maxima_solicitada").toLocalDate());
				result.setStgdte(rs.getTimestamp("stgdte").toLocalDateTime());

				result.setCentroCosto(rs.getString("centro_costo"));
				result.setTipoRemesa(rs.getString("tipo_remesa"));
				result.setClienteCodigoAlternoTms(rs.getString("cliente_codigo_alterno_tms"));
				result.setClienteDivision(rs.getString("cliente_division"));
				result.setSolicitudTipoDocumento(rs.getString("solicitud_tipo_documento"));
				result.setSolicitudNumeroDocumento(rs.getString("solicitud_numero_documento"));
				result.setOrdenAlistamientoNumeroDocumento(rs.getString("orden_alistamiento_numero_documento"));
				result.setRemisionTipoRemision(rs.getString("remision_tipo_remision"));
				result.setRemisionNumeroDocumento(rs.getString("remision_numero_documento"));

				result.setRemitenteTipoIdentificacion(rs.getString("remitente_tipo_identificacion"));
				result.setRemitenteIdentificacion(rs.getString("remitente_identificacion"));
				result.setRemitenteNombre(rs.getString("remitente_nombre"));
				result.setRemitenteDepartamento(rs.getString("remitente_departamento"));
				result.setRemitenteCiudad(rs.getString("remitente_ciudad"));
				result.setRemitenteDireccion(rs.getString("remitente_direccion"));
				result.setRemitenteTelefono(rs.getString("remitente_telefono"));
				result.setRemitenteContacto(rs.getString("remitente_contacto"));

				result.setDestinatarioTipoIdentificacion(rs.getString("destinatario_tipo_identificacion"));
				result.setDestinatarioIdentificacion(rs.getString("destinatario_identificacion"));
				result.setDestinatarioNombre(rs.getString("destinatario_nombre"));
				result.setDestinatarioDepartamento(rs.getString("destinatario_departamento"));
				result.setDestinatarioCiudad(rs.getString("destinatario_ciudad"));
				result.setDestinatarioDireccion(rs.getString("destinatario_direccion"));
				result.setDestinatarioTelefono(rs.getString("destinatario_telefono"));
				result.setDestinatarioContacto(rs.getString("destinatario_contacto"));
				result.setDestinatarioCiudadZona(rs.getString("destinatario_ciudad_zona"));
				result.setDestinatarioCoordenadaX(rs.getBigDecimal("destinatario_coordenada_x"));
				result.setDestinatarioCoordenadaY(rs.getBigDecimal("destinatario_coordenada_y"));

				result.setFechaCompromisoInicial(rs.getString("fecha_compromiso_inicial"));
				result.setFechaCompromisoFinal(rs.getString("fecha_compromiso_final"));
				result.setHoraCompromisoInicial(rs.getString("hora_compromiso_inicial"));
				result.setHoraCompromisoFinal(rs.getString("hora_compromiso_final"));

				result.setPlacaVehiculo(rs.getString("placa_vehiculo"));
				result.setSecuenciaEntrega(rs.getInt("secuencia_entrega"));
				result.setSeguro(rs.getInt("seguro"));
				result.setTarifa(rs.getInt("tarifa"));
				result.setBodegaCodigoAlterno(rs.getString("bodega_codigo_alterno"));
				result.setPrograma(rs.getString("programa"));
				
				result.setCiudadCodigoAlterno(rs.getString("ciudad_codigo_alterno"));
				result.setCiudadNombreAlterno(rs.getString("ciudad_nombre_alterno"));
				
				result.setPuntoCodigoAlterno(rs.getString("punto_codigo_alterno"));
				result.setPuntoNombreAlterno(rs.getString("punto_nombre_alterno"));
				result.setRegional(rs.getString("regional"));
				result.setPlanta(rs.getString("planta"));
				
				result.setResponsablePrincipal(rs.getString("responsable_principal"));
				result.setResponsableSuplente(rs.getString("responsable_suplente"));
				result.setRemesaObservacion(rs.getString("remesa_observacion"));

				result.setIdProducto(rs.getLong("id_producto"));
				result.setProductoCodigo(rs.getString("producto_codigo"));
				result.setProductoNombre(rs.getString("producto_nombre"));
				result.setIdEstadoInventario(rs.getString("id_estado_inventario"));
				result.setEstadoInventarioNombre(rs.getString("estado_inventario_nombre"));
				result.setLote(rs.getString("lote"));
				result.setFechaVencimiento(rs.getString("fecha_vencimiento"));

				result.setCantidad(rs.getBigDecimal("cantidad"));
				result.setUnidadMedidaCodigoAlternoTms(rs.getString("unidad_medida_codigo_alterno_tms"));
				result.setFactorConversion(rs.getInt("factor_conversion"));
				result.setCantidadEmpaques(rs.getBigDecimal("cantidad_empaques"));
				result.setUnidadEmpaqueCodigoAlternoTms(rs.getString("unidad_empaque_codigo_alterno_tms"));
				result.setPesoEmpaques(rs.getBigDecimal("peso_empaques"));
				result.setPesoBrutoEmpaques(rs.getBigDecimal("peso_empaques"));
				result.setVolumenEmpaques(rs.getBigDecimal("volumen_empaques"));
				result.setValorDeclarado(rs.getInt("valor_declarado"));
				result.setPredistribucion(rs.getString("predistribucion"));

				return result;
			}
		};
	}

	@Override
	public void marcarActasProcesadas(List<Long> id) {
		val sql = getSqlUpdate();

		val list = id.stream().distinct().collect(Collectors.toList());

		MapSqlParameterSource[] namedParameters = new MapSqlParameterSource[list.size()];
		int index = 0;
		for (val value : list) {
			namedParameters[index] = new MapSqlParameterSource();
			namedParameters[index].addValue("id", value);
			index++;
		}

		jdbcTemplate.batchUpdate(sql, namedParameters);

	}

	private String getSqlUpdate() {
		// @formatter:off
		return "" + 
				"UPDATE a \r\n" + 
				"SET \r\n" + 
				"     a.estado_solicitud = 'PROCESADO'\r\n" + 
				"    ,a.fecha_modificacion = GETDATE()\r\n" + 
				"    ,a.version = a.version + 1 \r\n" + 
				"FROM eConnect.dbo.solicitudes_actas_ingredion a \r\n" + 
				"WHERE \r\n" + 
				"    a.id_solicitud_acta = :id\r\n" + 
				"AND a.estado_solicitud = 'ORDEN_ALISTAMIENTO_EN_STAGE'\r\n"+
				"";
		// @formatter:on
	}

	@Override
	public void marcarActasEnvidas(RemesaDto model) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", model.getId());
		namedParameters.addValue("estado_solicitud", model.getEstadoIntegracion());
		namedParameters.addValue("subestado_solicitud", model.getSubEstadoIntegracion());
		namedParameters.addValue("reintentos", model.getReintentos());
		namedParameters.addValue("numero_confirmacion_silogtran", model.getNumeroConfirmacionSilogtran());
		namedParameters.addValue("fecha_integracion_silogtran", model.getFechaIntegracionSilogtran());
		
		val sql = getSqlUpdateEnvio();
		jdbcTemplate.update(sql, namedParameters);
	}
	
	private String getSqlUpdateEnvio() {
		// @formatter:off
		return "" + 
				"UPDATE a \r\n" + 
				"SET \r\n" + 
				"     a.estado_solicitud = :estado_solicitud\r\n" + 
				"    ,a.subestado_solicitud = :subestado_solicitud\r\n" +
				"    ,a.reintentos = :reintentos\r\n" +
				"    ,a.numero_confirmacion_silogtran = :numero_confirmacion_silogtran\r\n" +
				"    ,a.fecha_integracion_silogtran = :fecha_integracion_silogtran\r\n" +				
				"    ,a.fecha_modificacion = GETDATE()\r\n" + 
				"    ,a.version = a.version + 1 \r\n" + 
				"FROM eConnect.dbo.solicitudes_actas_ingredion a \r\n" + 
				"WHERE \r\n" + 
				"    a.id_solicitud_acta = :id\r\n" + 
				"";
		// @formatter:on
	}

	@Override
	public void errorDuranteEnvio(RemesaDto model, List<ErrorDto> errores) {
		marcarActasEnvidas(model);
		val sql = getSqlInsertError();

		MapSqlParameterSource[] parameters = new MapSqlParameterSource[errores.size()];
		int index = 0;
		for (val error : errores) {
			parameters[index] = new MapSqlParameterSource();
			parameters[index].addValue("idSolicitudActa", model.getId());
			parameters[index].addValue("codigo", error.getCodigo());
			parameters[index].addValue("mensaje", error.getMensaje());
			parameters[index].addValue("estadoNotificacion", error.getEstadoNotificacion());
			parameters[index].addValue("fechaCreacion", error.getFechaCreacion());
			parameters[index].addValue("fechaModificacion", error.getFechaModificacion());
			index++;
		}
		
		jdbcTemplate.batchUpdate(sql,parameters);
	}
	
	private String getSqlInsertError() {
		// @formatter:off
		return "" + 
				"INSERT INTO eConnect.dbo.solicitudes_actas_ingredion_errores\r\n" + 
				"    (id_solicitud_acta,codigo,mensaje,estado_notificacion,fecha_creacion,fecha_modificacion)\r\n" + 
				"VALUES\r\n" + 
				"    (:idSolicitudActa, :codigo, :mensaje, :estadoNotificacion,:fechaCreacion,:fechaModificacion)\r\n" +
				"";
		// @formatter:on
	}

}
