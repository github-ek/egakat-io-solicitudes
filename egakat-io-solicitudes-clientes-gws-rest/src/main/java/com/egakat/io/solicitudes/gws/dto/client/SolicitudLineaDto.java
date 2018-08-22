package com.egakat.io.solicitudes.gws.dto.client;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.dto.EntityDto;
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
@Builder
public class SolicitudLineaDto extends EntityDto<Integer> {

	@JsonProperty("lineNum")
	@NotNull
	@Size(max = 50)
	private String numeroLineaExterno;

	@JsonProperty("subLineNum")
	@NotNull
	@Size(max = 50)
	private String numeroSubLineaExterno;

	@JsonProperty("itemCode")
	@NotEmpty
	@Size(max = 50)
	private String productoCodigoAlterno;
	
	@JsonProperty("dscription")	
	@NotNull
	@Size(max = 50)
	private String productoNombre;

	@JsonProperty("quantityAsignada")	
	private int cantidad;

	@JsonProperty("whsCode")	
	@NotEmpty
	@Size(max = 50)
	private String bodegaCodigoAlterno;

	@NotNull
	@Size(max = 200)
	private String predistribucion;

	@Builder
	public SolicitudLineaDto(Integer id, @NotNull @Size(max = 50) String numeroLineaExterno,
			@NotNull @Size(max = 50) String numeroSubLineaExterno,
			@NotEmpty @Size(max = 50) String productoCodigoAlterno, @NotNull @Size(max = 50) String productoNombre,
			int cantidad, @NotEmpty @Size(max = 50) String bodegaCodigoAlterno,
			@NotNull @Size(max = 200) String predistribucion) {
		super(id);
		this.numeroLineaExterno = numeroLineaExterno;
		this.numeroSubLineaExterno = numeroSubLineaExterno;
		this.productoCodigoAlterno = productoCodigoAlterno;
		this.productoNombre = productoNombre;
		this.cantidad = cantidad;
		this.bodegaCodigoAlterno = bodegaCodigoAlterno;
		this.predistribucion = predistribucion;
	}
}
