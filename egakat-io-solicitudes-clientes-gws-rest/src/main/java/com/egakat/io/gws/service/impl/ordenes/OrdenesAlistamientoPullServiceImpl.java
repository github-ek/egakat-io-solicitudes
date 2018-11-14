package com.egakat.io.gws.service.impl.ordenes;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.service.impl.jdbc.JdbcPullServiceImpl;
import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenesAlistamientoPullServiceImpl extends JdbcPullServiceImpl<String>
		implements OrdenesAlistamientoPullService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO_EN_STAGE;
	}

	@Override
	public void pull() {
		val correlacion = defaultCorrelacion();

//		try {
//			val sql = getSql();
//			val result = getJdbcTemplate().queryForObject(sql, paramMap, getRowMapper());
//			return Optional.of(result);
//		} catch (DataAccessException e) {
//			return Optional.empty();
//		}
	}

	@Override
	protected String getSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RowMapper<String> getRowMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActualizacionDto asModel(String correlacion, String input) {
		// TODO Auto-generated method stub
		return null;
	}

}
