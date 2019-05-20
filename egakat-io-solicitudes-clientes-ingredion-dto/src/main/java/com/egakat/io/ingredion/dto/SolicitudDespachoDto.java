package com.egakat.io.ingredion.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SolicitudDespachoDto {
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@ApiModelProperty(notes = "Periodo", position = 1)
	@NotNull
	@Size(max = 50)
	private String periodo;

	@ApiModelProperty(notes = "Acta", position = 2)
	@NotNull
	@Size(max = 20)
	private String acta;

	@ApiModelProperty(notes = "ID", position = 3)
	@NotNull
	@Size(max = 50)
	private String identificador;

	@ApiModelProperty(notes = "Planta", position = 4)
	@NotNull
	@Size(max = 50)
	private String plantaCodigo;

	@ApiModelProperty(notes = "Bodega", position = 5)
	@NotNull
	@Size(max = 50)
	private String bodegaCodigoAlterno;

	@ApiModelProperty(notes = "CITA ENTREGA INI", position = 6, example = DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate fechaEntregaInicial;

	@ApiModelProperty(notes = "CITA ENTREGA FIN", position = 7, example = DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate fechaEntregaFinal;

	@ApiModelProperty(notes = "Cod Programa", position = 8)
	@NotNull
	@Size(max = 50)
	private String programaCodigo;

	@ApiModelProperty(notes = "Programas", position = 9)
	@NotNull
	@Size(max = 100)
	private String programaNombre;

	@ApiModelProperty(notes = "Cód. Regional", position = 10)
	@NotNull
	@Size(max = 50)
	private String regionalCodigo;

	@ApiModelProperty(notes = "Regional", position = 11)
	@NotNull
	@Size(max = 100)
	private String regionalNombre;

	@ApiModelProperty(notes = "Centro Zonal", position = 12)
	@NotNull
	@Size(max = 50)
	private String zonaCodigo;

	@ApiModelProperty(notes = "Cód. Municipio Punto", position = 13)
	@NotNull
	@Size(max = 50)
	private String ciudadCodigoAlterno;

	@ApiModelProperty(notes = "Municipio Punto", position = 14)
	@NotNull
	@Size(max = 100)
	private String ciudadNombre;

	@ApiModelProperty(notes = "Dirección", position = 15)
	@NotNull
	@Size(max = 150)
	private String direccion;

	@ApiModelProperty(notes = "Cód Punto de Entrega", position = 16)
	@NotNull
	@Size(max = 50)
	private String puntoCodigo;

	@ApiModelProperty(notes = "Punto de Entrega", position = 16)
	@NotNull
	@Size(max = 100)
	private String puntoNombre;

	@ApiModelProperty(notes = "Responsable Principal", position = 17)
	@NotNull
	@Size(max = 100)
	private String responsablePrincipal;

	@ApiModelProperty(notes = "Responsable Suplente", position = 18)
	@NotNull
	@Size(max = 100)
	private String responsableSuplente;

	@ApiModelProperty(notes = "Teléfono", position = 19)
	@NotNull
	@Size(max = 50)
	private String telefono;

	@ApiModelProperty(notes = "Líneas", position = 20)
	private List<LineaDto> lineas;

	@Getter
	@Setter
	@ToString(callSuper = true)
	@NoArgsConstructor
	static class LineaDto {
		@ApiModelProperty(notes = "Producto", position = 1)
		@NotEmpty
		@Size(max = 50)
		private String productoCodigo;

		@ApiModelProperty(notes = "Unidades Minimas", position = 2)
		private int unidades;

		@ApiModelProperty(notes = "Peso KG", position = 3)
		@NotNull
		@Size(max = 50)
		private String pesoInformativo;

		@ApiModelProperty(notes = "Cantidad  Embalaje", position = 4)
		@NotNull
		@Size(max = 50)
		private String cantidadEmbalajeInformativo;
	}
}
