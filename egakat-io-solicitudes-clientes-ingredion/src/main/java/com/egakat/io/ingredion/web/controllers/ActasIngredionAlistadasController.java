package com.egakat.io.ingredion.web.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egakat.io.ingredion.dto.ActaDto;
import com.egakat.io.ingredion.service.api.ActasIngredionAlistadasService;

import lombok.val;

@RestController
@RequestMapping(value = "/api/actas-alistadas", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ActasIngredionAlistadasController {

	private static final String FORMATO_DATE = "yyyy-MM-dd";

	@Autowired
	private ActasIngredionAlistadasService service;

	@GetMapping(path = "find-by/{argv}")
	public List<ActaDto> getActasAlistadas(
	// @formatter:off
		@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaDesde,
		@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaHasta,
		@MatrixVariable(required = false, defaultValue = "") List<String> estados,
		@MatrixVariable(required = false, defaultValue = "") List<Long> bodegas,
		@MatrixVariable(required = false, defaultValue = "") List<Long> origenes
		// @formatter:on
	) {
		val result = service.getActasAlistadas(fechaDesde, fechaHasta, estados, bodegas, origenes);
		return result;
	}

	@PostMapping(path = "/check")
	public void marcarActasProcesadas(@RequestBody List<Long> id) {
		service.marcarActasProcesadas(id);
	}
}
