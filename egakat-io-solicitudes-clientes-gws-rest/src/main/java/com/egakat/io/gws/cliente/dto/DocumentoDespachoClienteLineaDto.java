package com.egakat.io.gws.cliente.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.dto.EntityDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DocumentoDespachoClienteLineaDto extends EntityDto<Integer> {

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
	
}
