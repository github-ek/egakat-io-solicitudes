package com.egakat.io.gws.commons.ordenes.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.io.stage.dto.IntegrationEntityDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrdenAlistamientoDto extends IntegrationEntityDto {

	@NotNull
	@Size(max = 32)
	private String clientId;

	@NotNull
	@Size(max = 32)
	private String whId;

	@NotNull
	@Size(max = 35)
	private String ordnum;

	@NotNull
	@Size(max = 4)
	private String ordtyp;
	
	private Long idCliente;

	private Long idBodega;

	private Long idOrden;

	private List<OrdenAlistamientoLineaDto> lineas = new ArrayList<>();

}