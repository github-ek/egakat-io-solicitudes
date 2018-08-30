package com.egakat.io.solicitudes.gws.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.dto.SimpleEntityDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

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
abstract public class DataQualityEntityDto extends SimpleEntityDto<Long> {

	@NotNull
	@Size(max = 50)
	private String integracion;

	@NotNull
	@Size(max = 100)
	private String correlacion;

	@NotNull
	@Size(max = 100)
	private String idExterno;

	@NotNull
	private EstadoIntegracionType estadoIntegracion;

	public boolean hasErrors() {
		switch (getEstadoIntegracion()) {
		case ERROR_ENRIQUECIMIENTO:
		case ERROR_HOMOLOGACION:
		case ERROR_VALIDACION:
		case ERROR_CARGUE:
			return true;
		default:
			return false;
		}
	}

	public DataQualityEntityDto(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String correlacion,
			@NotNull @Size(max = 100) String idExterno, @NotNull EstadoIntegracionType estadoIntegracion) {
		super(id, version, fechaCreacion, fechaModificacion);
		this.integracion = integracion;
		this.correlacion = correlacion;
		this.idExterno = idExterno;
		this.estadoIntegracion = estadoIntegracion;
	}
}
