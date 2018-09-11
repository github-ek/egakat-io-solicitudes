package com.egakat.io.gws.ordenes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class OrdenAlistamientoClienteLoteDto {

    @JsonProperty("lote")
    private String lote;

    @JsonProperty("cantidad")
    private int cantidad;
    
    @JsonProperty("estadoOpl")
    private String estadoInventario;

}
