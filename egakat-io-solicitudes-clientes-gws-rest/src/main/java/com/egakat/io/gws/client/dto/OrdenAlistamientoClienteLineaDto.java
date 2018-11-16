
package com.egakat.io.gws.client.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrdenAlistamientoClienteLineaDto {

	@JsonIgnore
	private int numeroLinea;
	
	@JsonProperty("lineNum")
	private String numeroLineaExterno;

	@JsonProperty("subLineNum")
	private String numeroSublineaExterno;

	@JsonProperty("itemCode")
	private String productoCodigo;

	@JsonProperty("whsCode")
	private String bodegaCodigoAlterno;

	@JsonProperty("cantDespachada")
	private int cantidadDespachada;

	@JsonProperty("cantNoDespachada")
	private int cantidadNoDespachada;

	@JsonProperty("estadoOpl")
	private String estadoInventario;
	
	@JsonProperty("bodegaOpl")
	private String bodegaCodigo;
	
	@JsonProperty("alistamientoNovedades")
	private List<OrdenAlistamientoClienteCancelacionDto> cancelaciones;

	@JsonProperty("lotes")
	private List<OrdenAlistamientoClienteLoteDto> lotes;


}
