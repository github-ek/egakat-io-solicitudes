package com.egakat.io.gws.service.impl.ordenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.enums.EstadoNotificacionType;
import com.egakat.io.core.service.impl.jdbc.JdbcPullServiceImpl;
import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenesAlistamientoPullServiceImpl extends JdbcPullServiceImpl<Map<String, Object>>
		implements OrdenesAlistamientoPullService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO;
	}

	@Override
	public void pull() {
		val correlacion = defaultCorrelacion();

		try {
			val format = "integracion={}, correlacion= {}";
			log.debug(format, getIntegracion(), correlacion);

			val inputs = pull("");

			enqueue(correlacion, inputs);
		} catch (RuntimeException e) {
			getErroresService().create(getIntegracion(), correlacion, "", e);
			log.error("Exception:", e);
		}
	}

	protected List<Map<String, Object>> pull(Object... arg) {
		val sql = getSql();
		val paramMap = new HashMap<String, Object>();
		val result = getJdbcTemplate().queryForList(sql, paramMap);
		return result;
	}

	@Override
	protected String getSql() {
		val result = "SELECT * FROM dbo.OrdenesAlistamientoMensajesNuevos()";
		return result;
	}

	@Override
	protected ActualizacionDto asModel(String correlacion, Map<String, Object> input) {
		val result = new ActualizacionDto();

		result.setIntegracion(getIntegracion());
		result.setCorrelacion(correlacion);
		result.setIdExterno((String) input.get("id_externo"));
		result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
		result.setSubEstadoIntegracion("");
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);
		result.setArg0((String) input.get("wh_id"));
		result.setArg1((String) input.get("client_id"));
		result.setArg2((String) input.get("ordnum"));
		result.setArg3(String.valueOf(input.get("id_mensaje")));
		result.setArg4(String.valueOf(input.get("id_orden_alistamiento")));

		return result;
	}
}
