package com.egakat.io.clientes.gws.service.impl.solicitudes;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.enums.EstadoNotificacionType;
import com.egakat.integration.service.impl.rest.RestPushServiceImpl;
import com.egakat.io.clientes.gws.components.GwsRestClient;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.RestConstants;
import com.egakat.io.clientes.gws.constants.SolicitudDespachoClienteEstadoConstants;
import com.egakat.io.clientes.gws.dto.OrdenAlistamientoClienteCancelacionDto;
import com.egakat.io.clientes.gws.dto.OrdenAlistamientoClienteDto;
import com.egakat.io.clientes.gws.dto.OrdenAlistamientoClienteLineaDto;
import com.egakat.io.clientes.gws.dto.OrdenAlistamientoClienteLoteDto;
import com.egakat.io.clientes.gws.properties.SolicitudesDespachoRestProperties;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoNotificacionStagePushService;

import lombok.val;

@Service
public class SolicitudesDespachoNotificacionStagePushServiceImpl
		extends RestPushServiceImpl<Object, OrdenAlistamientoClienteDto, String>
		implements SolicitudesDespachoNotificacionStagePushService {

	private static final String ORDEN_DE_ALISTAMIENTO_LOTES_QUERY = "SELECT * FROM dbo.OrdenDeAlistamientoLotes(:id_orden_alistamiento)";

	private static final String ORDEN_DE_ALISTAMIENTO_CANCELACIONES_QUERY = "SELECT * FROM dbo.OrdenDeAlistamientoCancelaciones(:id_orden_alistamiento)";

	private static final String ORDEN_DE_ALISTAMIENTO_LINEAS_QUERY = "SELECT * FROM dbo.OrdenDeAlistamientoLineas(:id_orden_alistamiento)";

	private static final String ORDEN_DE_ALISTAMIENTO_QUERY = "SELECT * FROM dbo.OrdenDeAlistamiento(:integracion,:id_externo)";

	@Autowired
	private SolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.ORDENES_ALISTAMIENTO;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getOperacion() {
		val result = String.format("PUSH NOTIFICACION STAGE %s", getIntegracion());
		return result;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.CARGADO;
		val subestado = "STAGE";

		val result = getActualizacionesService()
				.findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(getIntegracion(), estado, subestado);
		return result;
	}

	@Override
	protected Object getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return null;
	}

	@Override
	protected OrdenAlistamientoClienteDto asOutput(Object input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val sql = ORDEN_DE_ALISTAMIENTO_QUERY;

		// @formatter:off
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("integracion", actualizacion.getIntegracion())
				.addValue("id_externo", actualizacion.getIdExterno());
		// @formatter:on

		val result = jdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> {
			OrdenAlistamientoClienteDto obj = new OrdenAlistamientoClienteDto();

			obj.setId(rs.getString("id_externo"));
			obj.setIdOrdenAlistamiento(rs.getLong("id_orden_alistamiento"));
			obj.setNumeroOrden(rs.getString("numero_orden"));
			obj.setTipoOrden(rs.getString("tipo_orden"));

			obj.setLineas(asLineas(obj.getIdOrdenAlistamiento()));

			return obj;
		});

		return result;
	}

	private List<OrdenAlistamientoClienteLineaDto> asLineas(long id_orden_alistamiento) {
		val result = getLineas(id_orden_alistamiento);
		val cancelaciones = getCancelaciones(id_orden_alistamiento);
		val lotes = getLotes(id_orden_alistamiento);

		for (val linea : result) {
			val c = cancelaciones.stream().filter(a -> a.getNumeroLinea() == linea.getNumeroLinea()).collect(toList());
			val l = lotes.stream().filter(a -> a.getNumeroLinea() == linea.getNumeroLinea()).collect(toList());
			linea.setCancelaciones(c);
			linea.setLotes(l);
		}

		return result;
	}

	private List<OrdenAlistamientoClienteLineaDto> getLineas(long id_orden_alistamiento) {
		val sql = ORDEN_DE_ALISTAMIENTO_LINEAS_QUERY;

		// @formatter:off
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("id_orden_alistamiento", id_orden_alistamiento);
		// @formatter:on

		val result = jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> {
			int cantidad_solicitada = rs.getInt("cantidad_solicitada");
			int unidades_alistadas = rs.getInt("unidades_alistadas");
			int cantidad_no_despachada = cantidad_solicitada - unidades_alistadas;

			if (cantidad_no_despachada < 0) {
				cantidad_no_despachada = 0;
			}

			OrdenAlistamientoClienteLineaDto m = new OrdenAlistamientoClienteLineaDto();
			m.setNumeroLinea(rs.getInt("numero_linea"));
			m.setNumeroLineaExterno(rs.getString("numero_linea_externo"));
			m.setNumeroSublineaExterno(rs.getString("numero_sublinea_externo"));
			m.setProductoCodigo(rs.getString("producto_codigo"));
			m.setBodegaCodigoAlterno(rs.getString("bodega_codigo_alterno"));
			m.setBodegaCodigo(rs.getString("bodega_codigo"));
			m.setEstadoInventario(rs.getString("id_estado_inventario"));
			m.setCantidadDespachada(unidades_alistadas);
			m.setCantidadNoDespachada(cantidad_no_despachada);

			return m;
		});
		return result;
	}

	private List<OrdenAlistamientoClienteCancelacionDto> getCancelaciones(long id_orden_alistamiento) {
		val sql = ORDEN_DE_ALISTAMIENTO_CANCELACIONES_QUERY;

		// @formatter:off
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("id_orden_alistamiento", id_orden_alistamiento);
		// @formatter:on

		val result = jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> {
			OrdenAlistamientoClienteCancelacionDto m = new OrdenAlistamientoClienteCancelacionDto();

			m.setNumeroLinea(rs.getInt("numero_linea"));
			m.setCausal(rs.getString("cancelacion_codigo"));
			m.setCantidad(rs.getInt("unidades_canceladas"));

			return m;
		});
		return result;
	}

	private List<OrdenAlistamientoClienteLoteDto> getLotes(long id_orden_alistamiento) {
		val sql = ORDEN_DE_ALISTAMIENTO_LOTES_QUERY;

		// @formatter:off
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("id_orden_alistamiento", id_orden_alistamiento);
		// @formatter:on

		val result = jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> {
			OrdenAlistamientoClienteLoteDto m = new OrdenAlistamientoClienteLoteDto();

			m.setNumeroLinea(rs.getInt("numero_linea"));
			m.setLote(rs.getString("lote"));
			m.setCantidad(rs.getInt("unidades_alistadas"));
			m.setEstadoInventario(rs.getString("id_estado_inventario"));

			return m;
		});
		return result;
	}

	@Override
	protected String push(OrdenAlistamientoClienteDto output, Object input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();

		val response = getRestClient().post(url, output, String.class);
		return response.getBody();
	}

	@Override
	protected void onSuccess(String result, OrdenAlistamientoClienteDto output, Object input,
			ActualizacionDto actualizacion) {
		val subestado = SolicitudDespachoClienteEstadoConstants.ALISTADA_OPL;
		val estadoNotificacion = EstadoNotificacionType.NOTIFICADA;

		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setEstadoNotificacion(estadoNotificacion);
		actualizacion.setReintentos(0);
		actualizacion.setDatos(result);
	}

	@Override
	protected void updateOnSuccess(String result, OrdenAlistamientoClienteDto output, Object input,
			ActualizacionDto actualizacion) {
		getActualizacionesService().update(actualizacion);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val subestado = "ERROR_NOTIFICANDO_STAGE";
		val estadoNotificacion = EstadoNotificacionType.ERROR;
		
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setEstadoNotificacion(estadoNotificacion);
		actualizacion.setReintentos(0);
	}
}