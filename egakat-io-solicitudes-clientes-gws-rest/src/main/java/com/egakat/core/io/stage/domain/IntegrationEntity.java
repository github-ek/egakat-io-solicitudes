package com.egakat.core.io.stage.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.data.jpa.domain.SimpleAuditableEntity;

import lombok.AccessLevel;
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

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	@Setter(value = AccessLevel.PROTECTED)
	private Long id;

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
