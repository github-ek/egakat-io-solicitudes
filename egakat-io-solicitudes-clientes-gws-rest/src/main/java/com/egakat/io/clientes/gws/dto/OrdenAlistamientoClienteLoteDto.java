package com.egakat.io.clientes.gws.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrdenAlistamientoClienteLoteDto {
	
	@JsonIgnore
	private int numeroLinea;

    @JsonProperty("lote")
    private String lote;

    @JsonProperty("cantidad")
    private int cantidad;
    
    @JsonProperty("estadoOpl")
    private String estadoInventario;

}
