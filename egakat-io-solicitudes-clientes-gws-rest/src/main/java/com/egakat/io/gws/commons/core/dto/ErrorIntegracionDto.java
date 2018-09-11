package com.egakat.io.gws.commons.core.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.egakat.io.gws.commons.core.enums.EstadoNotificacionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorIntegracionDto extends IntegrationEntityDto {

	@NotNull
	private EstadoNotificacionType estadoNotificacion;

	@DateTimeFormat(style = "M-")
	private LocalDateTime fechaNotificacion;

	@NotNull
	@Size(max = 100)
	private String codigo;

	@NotNull
	private String mensaje;

	@Size(max = 100)
	private String arg0;

	@Size(max = 100)
	private String arg1;

	@Size(max = 100)
	private String arg2;

	@Size(max = 100)
	private String arg3;

	@Size(max = 100)
	private String arg4;

	@Size(max = 100)
	private String arg5;

	@Size(max = 100)
	private String arg6;

	@Size(max = 100)
	private String arg7;

	@Size(max = 100)
	private String arg8;

	@Size(max = 100)
	private String arg9;

	public String getMensaje() {
		if (mensaje == null) {
			mensaje = "";
		}
		return mensaje;
	}

	public String getArg0() {
		if (arg0 == null) {
			arg0 = "";
		}
		return arg0;
	}

	public String getArg1() {
		if (arg1 == null) {
			arg1 = "";
		}
		return arg1;
	}

	public String getArg2() {
		if (arg2 == null) {
			arg2 = "";
		}
		return arg2;
	}

	public String getArg3() {
		if (arg3 == null) {
			arg3 = "";
		}
		return arg3;
	}

	public String getArg4() {
		if (arg4 == null) {
			arg4 = "";
		}
		return arg4;
	}

	public String getArg5() {
		if (arg5 == null) {
			arg5 = "";
		}
		return arg5;
	}

	public String getArg6() {
		if (arg6 == null) {
			arg6 = "";
		}
		return arg6;
	}

	public String getArg7() {
		if (arg7 == null) {
			arg7 = "";
		}
		return arg7;
	}

	public String getArg8() {
		if (arg8 == null) {
			arg8 = "";
		}
		return arg8;
	}

	public String getArg9() {
		if (arg9 == null) {
			arg9 = "";
		}
		return arg9;
	}

	@Builder
	public ErrorIntegracionDto(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String correlacion,
			@NotNull @Size(max = 100) String idExterno, @NotNull EstadoNotificacionType estadoNotificacion,
			LocalDateTime fechaNotificacion, @NotNull @Size(max = 100) String codigo, @NotNull String mensaje,
			@Size(max = 100) String arg0, @Size(max = 100) String arg1, @Size(max = 100) String arg2,
			@Size(max = 100) String arg3, @Size(max = 100) String arg4, @Size(max = 100) String arg5,
			@Size(max = 100) String arg6, @Size(max = 100) String arg7, @Size(max = 100) String arg8,
			@Size(max = 100) String arg9) {
		super(id, version, fechaCreacion, fechaModificacion, integracion, correlacion, idExterno);
		this.estadoNotificacion = estadoNotificacion;
		this.fechaNotificacion = fechaNotificacion;
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.arg0 = arg0;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.arg4 = arg4;
		this.arg5 = arg5;
		this.arg6 = arg6;
		this.arg7 = arg7;
		this.arg8 = arg8;
		this.arg9 = arg9;
	}
}
