
package com.egakat.io.gws.cliente.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@ToString
public class OrdenAlistamientoClienteDto {

	@JsonIgnore
	private long idOrdenAlistamiento;
	
	@JsonProperty("idSolicitud")
	private String id;

	@JsonProperty("numeroOrdenAlistamiento")
	private String numeroOrden;

	@JsonProperty("tipoOrden")
	private String tipoOrden;

	@JsonProperty("alistamientoLineas")
	private List<OrdenAlistamientoClienteLineaDto> lineas;
}
