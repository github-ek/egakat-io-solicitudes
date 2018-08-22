package com.egakat.io.solicitudes.gws.dto.client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.dto.EntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SolicitudDto extends EntityDto<Integer> {

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

	private List<SolicitudLineaDto> lineas;
	
	@Builder
	public SolicitudDto(Integer id, @NotNull @Size(max = 20) String clienteCodigoAlterno,
			@NotNull @Size(max = 50) String servicioCodigoAlterno, @NotNull @Size(max = 20) String prefijo,
			@NotNull @Size(max = 20) String numeroSolicitudSinPrefijo, @NotNull LocalDate femi, @NotNull LocalDate fema,
			LocalTime homi, LocalTime homa, @NotNull @Size(max = 20) String terceroIdentificacion,
			@NotNull @Size(max = 100) String terceroNombre, @NotNull @Size(max = 50) String canalCodigoAlterno,
			@NotNull @Size(max = 50) String ciudadCodigoAlterno, @NotNull @Size(max = 150) String direccion,
			@NotNull @Size(max = 50) String puntoCodigoAlterno, @NotNull @Size(max = 20) String numeroOrdenCompra,
			@NotNull @Size(max = 200) String nota, List<SolicitudLineaDto> lineas) {
		super(id);
		this.clienteCodigoAlterno = clienteCodigoAlterno;
		this.servicioCodigoAlterno = servicioCodigoAlterno;
		this.prefijo = prefijo;
		this.numeroSolicitudSinPrefijo = numeroSolicitudSinPrefijo;
		this.femi = femi;
		this.fema = fema;
		this.homi = homi;
		this.homa = homa;
		this.terceroIdentificacion = terceroIdentificacion;
		this.terceroNombre = terceroNombre;
		this.canalCodigoAlterno = canalCodigoAlterno;
		this.ciudadCodigoAlterno = ciudadCodigoAlterno;
		this.direccion = direccion;
		this.puntoCodigoAlterno = puntoCodigoAlterno;
		this.numeroOrdenCompra = numeroOrdenCompra;
		this.nota = nota;
		this.lineas = lineas;
	}
}