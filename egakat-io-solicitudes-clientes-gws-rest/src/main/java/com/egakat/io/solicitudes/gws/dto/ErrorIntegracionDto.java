package com.egakat.io.solicitudes.gws.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestClientResponseException;

import com.egakat.commons.dto.SimpleEntityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorIntegracionDto extends SimpleEntityDto<Long> {

	@NotNull
	@Size(max = 50)
	private String integracion;

	@NotNull
	@Size(max = 100)
	private String idExterno;

	@NotNull
	@Size(max = 100)
	private String correlacion;

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

	@Builder
	public ErrorIntegracionDto(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String idExterno,
			@NotNull @Size(max = 100) String correlacion, @NotNull @Size(max = 100) String codigo,
			@NotNull String mensaje, @Size(max = 100) String arg0, @Size(max = 100) String arg1,
			@Size(max = 100) String arg2, @Size(max = 100) String arg3, @Size(max = 100) String arg4,
			@Size(max = 100) String arg5, @Size(max = 100) String arg6, @Size(max = 100) String arg7,
			@Size(max = 100) String arg8, @Size(max = 100) String arg9) {
		super(id, version, fechaCreacion, fechaModificacion);
		this.integracion = integracion;
		this.idExterno = idExterno;
		this.correlacion = correlacion;
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

	public static ErrorIntegracionDto error(String integracion, String idExterno, String correlacion, String codigo,
			String mensaje, String... arg) {
		val argumentos = normalizarArgumentos(arg);

		// @formatter:off
		val result = ErrorIntegracionDto
				.builder()
				.integracion(integracion)
				.idExterno(idExterno)
				.correlacion(correlacion)
				.codigo(StringUtils.left(codigo, 100))
				.mensaje(mensaje)
				.arg0(argumentos[0])
				.arg1(argumentos[1])
				.arg2(argumentos[2])
				.arg3(argumentos[3])
				.arg4(argumentos[4])
				.arg5(argumentos[5])
				.arg6(argumentos[6])
				.arg7(argumentos[7])
				.arg8(argumentos[8])
				.arg9(argumentos[9])				
				.build();
		// @formatter:on

		return result;
	}

	public static ErrorIntegracionDto error(String integracion, String idExterno, String correlacion, String codigo,
			Throwable t) {
		val c = StringUtils.isEmpty(codigo) ? t.getClass().getName() : codigo;
		String msg = t.getMessage();
		if (t instanceof RestClientResponseException) {
			msg = ((RestClientResponseException) t).getResponseBodyAsString();
		}
		val result = error(integracion, idExterno, correlacion, c, msg);
		return result;
	}

	protected static String[] normalizarArgumentos(String... arg) {
		val result = new String[10];

		int n = Math.min(result.length, arg.length);
		for (int i = 0; i < result.length; i++) {
			if (i < n) {
				result[i] = arg[i];
			} else {
				result[i] = "";
			}
		}

		return result;
	}
}
