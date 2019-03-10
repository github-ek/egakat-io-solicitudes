package com.egakat.io.ingredion.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorDto {
	private Long id;

	private String codigo;
	private String mensaje;
	private String estadoNotificacion;
	private LocalDateTime fechaNotificacion;

	private int version;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaModificacion;
}
