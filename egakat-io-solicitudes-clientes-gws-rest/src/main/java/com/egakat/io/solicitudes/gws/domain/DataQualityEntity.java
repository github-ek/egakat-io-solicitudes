package com.egakat.io.solicitudes.gws.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.domain.SimpleEntity;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
abstract public class DataQualityEntity extends SimpleEntity<Long> {

	@Column(name = "integracion", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String integracion;

	@Column(name = "correlacion", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String correlacion;

	@Column(name = "id_externo", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String idExterno;

	@Column(name = "estado_integracion", length = 50, nullable = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private EstadoIntegracionType estadoIntegracion;

	public DataQualityEntity(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String correlacion,
			@NotNull @Size(max = 100) String idExterno, @NotNull EstadoIntegracionType estadoIntegracion) {
		super(id, version, fechaCreacion, fechaModificacion);
		this.integracion = integracion;
		this.correlacion = correlacion;
		this.idExterno = idExterno;
		this.estadoIntegracion = estadoIntegracion;
	}
}
