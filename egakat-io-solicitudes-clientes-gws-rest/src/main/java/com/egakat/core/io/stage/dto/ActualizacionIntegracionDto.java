package com.egakat.core.io.stage.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ActualizacionIntegracionDto extends IntegrationEntityDto {

	@NotNull
	private EstadoIntegracionType estadoIntegracion;

	@Size(max = 50)
	@NotNull
	private String subEstadoIntegracion;
	@NotNull
	private EstadoNotificacionType estadoNotificacion;

	private int entradasEnCola;

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

	private String datos;

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

	public String getDatos() {
		if (datos == null) {
			datos = "";
		}
		return datos;
	}
}
