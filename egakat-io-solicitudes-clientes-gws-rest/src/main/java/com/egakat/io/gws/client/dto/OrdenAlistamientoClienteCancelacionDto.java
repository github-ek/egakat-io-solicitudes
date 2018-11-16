
package com.egakat.io.gws.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrdenAlistamientoClienteCancelacionDto {

	@JsonIgnore
	private int numeroLinea;
	
    @JsonProperty("causalNoDespacho")
    private String causal;
    
    @JsonProperty("cantidad")
    private int cantidad;
}
