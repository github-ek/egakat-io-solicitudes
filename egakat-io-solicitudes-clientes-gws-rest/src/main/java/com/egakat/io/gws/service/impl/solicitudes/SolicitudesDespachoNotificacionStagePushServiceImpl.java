package com.egakat.io.gws.service.impl.solicitudes;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.rest.RestPushNotificationServiceImpl;
import com.egakat.io.gws.client.components.GwsRestClient;
import com.egakat.io.gws.client.constants.GwsIntegracionesConstants;
import com.egakat.io.gws.client.constants.GwsRestConstants;
import com.egakat.io.gws.client.dto.OrdenAlistamientoClienteCancelacionDto;
import com.egakat.io.gws.client.dto.OrdenAlistamientoClienteDto;
import com.egakat.io.gws.client.dto.OrdenAlistamientoClienteLineaDto;
import com.egakat.io.gws.client.dto.OrdenAlistamientoClienteLoteDto;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;

import lombok.val;

@Service
public class SolicitudesDespachoNotificacionStagePushServiceImpl
		extends RestPushNotificationServiceImpl<OrdenAlistamientoClienteDto, String> {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

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
		return GwsRestConstants.ORDENES_ALISTAMIENTO;
	}

	@Override
	protected String getIntegracion() {
		return GwsIntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.CARGADO, "STAGE");
		return result;
	}

	@Override
	protected OrdenAlistamientoClienteDto asOutput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val sql = "SELECT * FROM dbo.OrdenDeAlistamiento(:integracion,:id_externo)";

		val paramMap = new HashMap<String, Object>();
		paramMap.put("integracion", actualizacion.getIntegracion());
		paramMap.put("id_externo", actualizacion.getIdExterno());

		val result = getJdbcTemplate().queryForObject(sql, paramMap, (rs, rowNum) -> {
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
			val c = cancelaciones.stream().filter(a -> a.getNumeroLinea() == linea.getNumeroLinea()).collect(Collectors.toList());
			val l = lotes.stream().filter(a -> a.getNumeroLinea() == linea.getNumeroLinea()).collect(Collectors.toList());
			linea.setCancelaciones(c);
			linea.setLotes(l);
		}

		return result;
	}

	private List<OrdenAlistamientoClienteLineaDto> getLineas(long id_orden_alistamiento) {
		val sql = "SELECT * FROM dbo.OrdenDeAlistamientoLineas(:id_orden_alistamiento)";

		val paramMap = new HashMap<String, Object>();
		paramMap.put("id_orden_alistamiento", id_orden_alistamiento);

		val result = getJdbcTemplate().query(sql, paramMap, (rs, rowNum) -> {
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
		val sql = "SELECT * FROM dbo.OrdenDeAlistamientoCancelaciones(:id_orden_alistamiento)";

		val paramMap = new HashMap<String, Object>();
		paramMap.put("id_orden_alistamiento", id_orden_alistamiento);

		val result = getJdbcTemplate().query(sql, paramMap, (rs, rowNum) -> {
			OrdenAlistamientoClienteCancelacionDto m = new OrdenAlistamientoClienteCancelacionDto();

			m.setNumeroLinea(rs.getInt("numero_linea"));
			m.setCausal(rs.getString("cancelacion_codigo"));
			m.setCantidad(rs.getInt("unidades_canceladas"));

			return m;
		});
		return result;
	}

	private List<OrdenAlistamientoClienteLoteDto> getLotes(long id_orden_alistamiento) {
		val sql = "SELECT * FROM dbo.OrdenDeAlistamientoLotes(:id_orden_alistamiento)";

		val paramMap = new HashMap<String, Object>();
		paramMap.put("id_orden_alistamiento", id_orden_alistamiento);

		val result = getJdbcTemplate().query(sql, paramMap, (rs, rowNum) -> {
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
	protected String push(OrdenAlistamientoClienteDto output, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();

		val response = getRestClient().post(url, output, String.class);
		return response.getBody();
	}

	@Override
	protected void onSuccess(String response, OrdenAlistamientoClienteDto output, ActualizacionDto actualizacion) {
		actualizacion.setSubEstadoIntegracion("");
		actualizacion.setReintentos(0);
		actualizacion.setDatos(response);
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		actualizacion.setSubEstadoIntegracion("ERROR_NOTIFICANDO_STAGE");
		actualizacion.setReintentos(0);
	}
}