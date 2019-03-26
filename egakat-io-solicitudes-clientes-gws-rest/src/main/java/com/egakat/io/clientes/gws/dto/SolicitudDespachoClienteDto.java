package com.egakat.io.clientes.gws.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.dto.EntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SolicitudDespachoClienteDto extends EntityDto<Integer> {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String TIME_FORMAT = "HH:mm";

	@JsonProperty("codCliente")
	@NotNull
	@Size(max = 20)
	private String clienteCodigoAlterno;

	@JsonProperty("tipoServicio")
	@NotNull
	@Size(max = 50)
	private String servicioCodigoAlterno;

	@JsonProperty("seriesName")
	@NotNull
	@Size(max = 20)
	private String prefijo;

	@JsonProperty("docNum")
	@NotNull
	@Size(max = 20)
	private String numeroSolicitudSinPrefijo;

	@JsonProperty("feMi")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate femi;

	@JsonProperty("feMa")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate fema;

	@JsonProperty("hoMi")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
	private LocalTime homi;

	@JsonProperty("hoMa")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
	private LocalTime homa;

	@JsonProperty("nit")
	@NotNull
	@Size(max = 20)
	private String terceroIdentificacion;

	@JsonProperty("razonSocial")
	@NotNull
	@Size(max = 100)
	private String terceroNombre;

	@JsonProperty("groupName")
	@NotNull
	@Size(max = 50)
	private String canalCodigoAlterno;

	@JsonProperty("codDane")
	@NotNull
	@Size(max = 50)
	private String ciudadCodigoAlterno;

	@NotNull
	@Size(max = 150)
	private String direccion;

	@JsonProperty("shipToCode")
	@NotNull
	@Size(max = 50)
	private String puntoCodigoAlterno;

	@JsonProperty("numAtCard")
	@NotNull
	@Size(max = 20)
	private String numeroOrdenCompra;

	@JsonProperty("comments")
	@NotNull
	@Size(max = 200)
	private String nota;

	private List<SolicitudDespachoClienteLineaDto> lineas;
}