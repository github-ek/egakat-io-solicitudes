package com.egakat.io.ingredion.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egakat.io.ingredion.dto.SolicitudDespachoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Solicitudes de despacho de Ingredion enviadas desde Silogtran")
@RestController
@RequestMapping(value = "/api/solictudes/despachos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
@Slf4j
public class SolicitudesDespachoController {

	@ApiOperation(value = "Crea una solicitud", response = SolicitudDespachoDto.class)
	@PostMapping()
	public ResponseEntity<Long> create(@RequestBody SolicitudDespachoDto model) {
		log.debug("solicitud:{}", model);
		return ResponseEntity.ok(System.currentTimeMillis());
	}

	//@ApiOperation(value = "Consulta una solicitud por su numero de acta", response = SolicitudDespachoDto.class)
	//@GetMapping
	public SolicitudDespachoDto get(@RequestParam("acta") String id) {
		return null;
	}

}
