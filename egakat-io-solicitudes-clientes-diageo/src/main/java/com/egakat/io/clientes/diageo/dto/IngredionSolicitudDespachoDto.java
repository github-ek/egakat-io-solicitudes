package com.egakat.io.clientes.diageo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.io.commons.solicitudes.dto.AbstractSolicitudDespachoDto;

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
public class IngredionSolicitudDespachoDto extends AbstractSolicitudDespachoDto<IngredionSolicitudDespachoLineaDto> {

	@NotNull
	@Size(max = 20)
	private String periodo;

	@NotNull
	@Size(max = 50)
	private String plantaCodigo;

	@NotNull
	@Size(max = 50)
	private String programaCodigo;

	@NotNull
	@Size(max = 100)
	private String programaNombre;

	@NotNull
	@Size(max = 50)
	private String regionalCodigo;

	@NotNull
	@Size(max = 100)
	private String regionalNombre;	
	
	@NotNull
	@Size(max = 50)
	private String zonaCodigo;

	@NotNull
	@Size(max = 100)
	private String ciudadNombre;	
}