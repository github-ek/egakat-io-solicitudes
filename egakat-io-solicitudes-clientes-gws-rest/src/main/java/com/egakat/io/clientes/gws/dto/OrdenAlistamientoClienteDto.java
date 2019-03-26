
package com.egakat.io.clientes.gws.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrdenAlistamientoClienteDto {

	@JsonProperty("idSolicitud")
	private String id;

	@JsonIgnore
	private long idOrdenAlistamiento;

	@JsonProperty("numeroOrdenAlistamiento")
	private String numeroOrden;

	@JsonProperty("tipoOrden")
	private String tipoOrden;

	@JsonProperty("alistamientoLineas")
	private List<OrdenAlistamientoClienteLineaDto> lineas;
}
