package com.egakat.io.ingredion.alertas.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;

import lombok.val;

@Service
public class ErroresService {

	private static final int PARTITION_SIZE = 1000;

	private static final String NOTIFICAR = "NOTIFICAR";

	private static final String NOTIFICADO = "NOTIFICADO";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${cron-alertas-errores-consolidados-minus-hours:6}")
	private Long minusHours;

	public Collection<List<ErrorActaDto>> getActasConError() {
		val sql = getSqlSelectActasConErrores();

		val estados = Arrays.asList(NOTIFICAR);

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("estados", estados);

		val list = jdbcTemplate.query(sql, parameters, getActaRowMapper());

		for (val acta : list) {
			acta.getErrores().addAll(getErroresById(acta.getId(), estados));
		}

		val result = partition(list, PARTITION_SIZE);

		return result;
	}

	public Collection<List<ErrorActaDto>> getConsolidadoActasConError() {
		val sql = getSqlSelectActasConErrores() + " AND b.fecha_notificacion >= :fechaDesde\n";

		val fechaDesde = LocalDateTime.now().minusHours(minusHours).truncatedTo(ChronoUnit.SECONDS);
		val estados = Arrays.asList(NOTIFICADO);

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("estados", estados);
		parameters.addValue("fechaDesde", fechaDesde);

		val list = jdbcTemplate.query(sql, parameters, getActaRowMapper());

		for (val acta : list) {
			acta.getErrores().addAll(getErroresById(acta.getId(), estados));
		}

		val result = partition(list, PARTITION_SIZE);

		return result;
	}

	private List<ErrorIntegracionDto> getErroresById(Long id, List<String> estados) {

		val sql = getSqlSelectErrores();

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", id);
		parameters.addValue("estados", estados);

		val result = jdbcTemplate.query(sql, parameters, getErrorRowMapper());

		return result;
	}

	@Transactional(readOnly = false)
	public void notificados(List<ErrorActaDto> actas) {
		val sql = getSqlUpdate();

		val ids = new ArrayList<Long>();

		actas.forEach(acta -> {
			val list = acta.getErrores().stream().map(a -> a.getId()).collect(Collectors.toList());
			ids.addAll(list);
		});

		MapSqlParameterSource[] namedParameters = new MapSqlParameterSource[ids.size()];
		int index = 0;
		for (val value : ids) {
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
				"     a.estado_notificacion = 'NOTIFICADO'\r\n" + 
				"    ,a.fecha_notificacion = GETDATE()\r\n" +
				"    ,a.fecha_modificacion = GETDATE()\r\n" + 
				"    ,a.version = a.version + 1 \r\n" + 
				"FROM [eConnect].dbo.solicitudes_actas_ingredion_errores a \r\n" + 
				"WHERE \r\n" + 
				"    a.id = :id\r\n" + 
				"";
		// @formatter:on
	}

	private static <T> Collection<List<T>> partition(List<T> list, int size) {
		final AtomicInteger counter = new AtomicInteger(0);

		return list.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size)).values();
	}

	// -------------------------------------------------------------------------------------------------------------------------------------
	// --
	// -------------------------------------------------------------------------------------------------------------------------------------
	private RowMapper<ErrorActaDto> getActaRowMapper() {
		return new RowMapper<ErrorActaDto>() {
			@Override
			public ErrorActaDto mapRow(ResultSet rs, int i) throws SQLException {
				val result = new ErrorActaDto();

				result.setId(rs.getLong("id_solicitud_acta"));
				result.setNumeroSolicitud(rs.getString("numero_solicitud"));
				result.setEstadoIntegracion(rs.getString("estado_solicitud"));
				result.setSubEstadoIntegracion(rs.getString("subestado_solicitud"));

				result.setBodegaCodigoAlterno(rs.getString("bodega_codigo_alterno"));
				result.setWhid(rs.getString("wh_id"));
				result.setOrdnum(rs.getString("ordnum"));

				result.setCiudadCodigoAlterno(rs.getString("ciudad_codigo_alterno"));
				result.setCiudadNombreAlterno(rs.getString("ciudad_nombre_alterno"));
				result.setDestinatarioDireccion(rs.getString("destinatario_direccion"));
				result.setDestinatarioIdentificacion(rs.getString("destinatario_identificacion"));
				result.setDestinatarioNombre(rs.getString("destinatario_nombre"));

				return result;
			}
		};
	}

	private RowMapper<ErrorIntegracionDto> getErrorRowMapper() {
		return new RowMapper<ErrorIntegracionDto>() {
			@Override
			public ErrorIntegracionDto mapRow(ResultSet rs, int i) throws SQLException {
				val result = new ErrorIntegracionDto();

				result.setId(rs.getLong("id"));
				result.setCodigo(rs.getString("codigo"));
				result.setMensaje(rs.getString("mensaje"));
				//result.setEstadoNotificacion(rs.getString("estado_notificacion"));
				result.setFechaNotificacion(getLocalDateTime(rs, "fecha_notificacion"));

				result.setVersion(rs.getInt("version"));
				result.setFechaCreacion(getLocalDateTime(rs, "fecha_creacion"));
				result.setFechaModificacion(getLocalDateTime(rs, "fecha_modificacion"));

				return result;
			}

			private LocalDateTime getLocalDateTime(ResultSet rs, String columLabel) throws SQLException {
				val sqlTimestamp = rs.getTimestamp(columLabel);
				val result = (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
				return result;
			}
		};
	}

	private String getSqlSelectActasConErrores() {
		// @formatter:off
		val result = ""
				+ "SELECT DISTINCT \n" + 
				"     a.id_solicitud_acta\n" + 
				"    ,a.numero_solicitud\n" + 
				"    ,a.estado_solicitud\n" + 
				"    ,a.subestado_solicitud\n" + 
				"\n" + 
				"    ,a.bodega_codigo_alterno\n" + 
				"    ,a.wh_id\n" + 
				"    ,a.ordnum\n" + 
				"\n" + 
				"    ,a.ciudad_codigo_alterno\n" + 
				"    ,a.ciudad_nombre_alterno\n" + 
				"    ,a.destinatario_direccion\n" + 
				"    ,a.destinatario_identificacion\n" + 
				"    ,a.destinatario_nombre\n" + 
				"FROM [eConnect].dbo.solicitudes_actas_ingredion a\n" + 
				"INNER JOIN [eConnect].dbo.solicitudes_actas_ingredion_errores b ON\n" + 
				"    b.id_solicitud_acta = a.id_solicitud_acta\n" + 
				"AND b.estado_notificacion IN (:estados)\n" + 
				"WHERE\n" + 
				"    a.estado_solicitud LIKE 'ERROR%'\n"
				+ "";
		// @formatter:on
		return result;
	}

	private static String getSqlSelectErrores() {
		// @formatter:off
		val result = "" + 
				"SELECT \n" + 
				"     a.* \n" +
				"FROM [eConnect].dbo.solicitudes_actas_ingredion_errores a \n" + 
				"WHERE\n" + 
				"    a.id_solicitud_acta = :id\n" + 
				"AND a.estado_notificacion IN (:estados)\n" + 
				"";
		// @formatter:on
		return result;
	}
}
