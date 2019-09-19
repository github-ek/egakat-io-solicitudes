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
@Table(name = "solicitudes_despacho_cliente_ingredion_lineas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class IngredionSolicitudDespachoLinea extends BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	@Setter(value = AccessLevel.PROTECTED)
	private Long id;
	
	@Column(name = "id_solicitud_despacho", nullable = false)
	private long idSolicitudDespacho;

	@Column(name = "numero_linea")
	private int numeroLinea;
    
	@Column(name = "peso_informativo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String pesoInformativo;

	@Column(name = "cantidad_embalaje_informativo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String cantidadEmbalajeInformativo;
}