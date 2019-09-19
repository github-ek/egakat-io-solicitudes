package com.egakat.io.clientes.ingredion.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egakat.io.clientes.ingredion.dto.SilogtranSolicitudDespachoDto;
import com.egakat.io.clientes.ingredion.service.api.IngredionSolicitudDespachoPostService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Solicitudes de despacho de Ingredion enviadas desde Silogtran")
@RestController
@RequestMapping(value = "/api/solictudes/despachos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
@Slf4j
public class IngredionSolicitudesDespachoController {

	@Autowired
	private IngredionSolicitudDespachoPostService postService;

	@ApiOperation(value = "Crea una solicitud", response = SilogtranSolicitudDespachoDto.class)
	@PostMapping()
	public ResponseEntity<Long> create(@RequestBody SilogtranSolicitudDespachoDto input) {
		log.debug("solicitud:{}", input);

		try {	
			val optional = postService.post(input);
			if(optional.isPresent()) {
				return ResponseEntity.ok(optional.get());
			}else {
				return ResponseEntity.ok().build();
			}
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
