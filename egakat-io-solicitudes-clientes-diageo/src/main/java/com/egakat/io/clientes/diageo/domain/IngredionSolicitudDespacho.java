package com.egakat.io.clientes.diageo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.core.data.jpa.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solicitudes_despacho_cliente_ingredion")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class IngredionSolicitudDespacho extends BaseEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	@Setter(value = AccessLevel.PROTECTED)
	private Long id;

	@Column(name = "id_solicitud_despacho", nullable = false)
	private long idSolicitudDespacho;
	
	@Column(name = "periodo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String periodo;

	@Column(name = "planta_codigo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String plantaCodigo;

	@Column(name = "programa_codigo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String programaCodigo;

	@Column(name = "programa_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String programaNombre;

	@Column(name = "regional_codigo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String regionalCodigo;

	@Column(name = "regional_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String regionalNombre;	
	
	@Column(name = "zona_codigo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String zonaCodigo;

	@Column(name = "ciudad_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String ciudadNombre;	
}