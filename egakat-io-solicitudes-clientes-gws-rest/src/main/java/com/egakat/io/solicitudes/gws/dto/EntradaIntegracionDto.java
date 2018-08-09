package com.egakat.io.solicitudes.gws.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.dto.BusinessEntityDto;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;

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
public class EntradaIntegracionDto extends BusinessEntityDto<Long> {

	@NotNull
	private EstadoEntradaIntegracionType estado;

	private boolean programarNotificacion;

	private boolean notificacionRealizada;

	@NotNull
	@Size(max = 50)
	private String integracion;

	@NotNull
	@Size(max = 100)
	private String idExterno;

	@NotNull
	@Size(max = 50)
	private String estadoExterno;

	private int entradasEnCola;

	@NotNull
	@Size(max = 100)
	private String arg0;

	@NotNull
	@Size(max = 100)
	private String arg1;

	@NotNull
	@Size(max = 100)
	private String arg2;

	@NotNull
	@Size(max = 100)
	private String arg3;

	@NotNull
	@Size(max = 100)
	private String arg4;

	@NotNull
	@Size(max = 100)
	private String arg5;

	@NotNull
	@Size(max = 100)
	private String arg6;

	@NotNull
	@Size(max = 100)
	private String arg7;

	@NotNull
	@Size(max = 100)
	private String arg8;

	@NotNull
	@Size(max = 100)
	private String arg9;

	@NotNull
	private String datos;

	@Builder
	public EntradaIntegracionDto(Long id, int version, String creadoPor, LocalDateTime fechaCreacion,
			String modificadoPor, LocalDateTime fechaModificacion, @NotNull EstadoEntradaIntegracionType estado,
			boolean programarNotificacion, boolean notificacionRealizada, @NotNull @Size(max = 50) String integracion,
			@NotNull @Size(max = 100) String idExterno, @NotNull @Size(max = 50) String estadoExterno,
			int entradasEnCola, @NotNull @Size(max = 100) String arg0, @NotNull @Size(max = 100) String arg1,
			@NotNull @Size(max = 100) String arg2, @NotNull @Size(max = 100) String arg3,
			@NotNull @Size(max = 100) String arg4, @NotNull @Size(max = 100) String arg5,
			@NotNull @Size(max = 100) String arg6, @NotNull @Size(max = 100) String arg7,
			@NotNull @Size(max = 100) String arg8, @NotNull @Size(max = 100) String arg9, @NotNull String datos) {
		super(id, version, creadoPor, fechaCreacion, modificadoPor, fechaModificacion);
		this.estado = estado;
		this.programarNotificacion = programarNotificacion;
		this.notificacionRealizada = notificacionRealizada;
		this.integracion = integracion;
		this.idExterno = idExterno;
		this.estadoExterno = estadoExterno;
		this.entradasEnCola = entradasEnCola;
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
		this.datos = datos;
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
		if(arg3 == null) {
			arg3 = "";
		}
		return arg3;
	}

	public String getArg4() {
		if(arg4 == null) {
			arg4 = "";
		}
		return arg4;
	}

	public String getArg5() {
		if(arg5 == null) {
			arg5 = "";
		}
		return arg5;
	}

	public String getArg6() {
		if(arg6 == null) {
			arg6 = "";
		}
		return arg6;
	}

	public String getArg7() {
		if(arg7 == null) {
			arg7 = "";
		}
		return arg7;
	}

	public String getArg8() {
		if(arg8 == null) {
			arg8 = "";
		}
		return arg8;
	}

	public String getArg9() {
		if(arg9 == null) {
			arg9 = "";
		}
		return arg9;
	}

	public String getDatos() {
		if(datos == null) {
			datos = "";
		}
		return datos;
	}
}