package com.egakat.io.gws.commons.core.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.domain.SimpleAuditableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
abstract public class IntegrationEntity extends SimpleAuditableEntity<Long> {

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
}
