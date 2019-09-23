package com.egakat.io.ingredion.alertas.dto;

import java.util.ArrayList;
import java.util.List;

import com.egakat.integration.dto.ErrorIntegracionDto;

import lombok.Data;

@Data
public class ErrorActaDto  {

	private Long id;
	private String numeroSolicitud;
	private String estadoIntegracion;
	private String subEstadoIntegracion;

	private String bodegaCodigoAlterno;
	private String whid;
	private String ordnum;

	private String ciudadCodigoAlterno;
	private String ciudadNombreAlterno;
	private String destinatarioDireccion;
	private String destinatarioIdentificacion;
	private String destinatarioNombre;
	
	private List<ErrorIntegracionDto> errores = new ArrayList<>();
}
