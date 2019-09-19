package com.egakat.io.clientes.ingredion.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;

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
public class IngredionSolicitudDespachoLineaDto extends SolicitudDespachoLineaDto {

	@NotNull
	@Size(max = 20)
	private String pesoInformativo;

	@NotNull
	@Size(max = 20)
	private String cantidadEmbalajeInformativo;

}