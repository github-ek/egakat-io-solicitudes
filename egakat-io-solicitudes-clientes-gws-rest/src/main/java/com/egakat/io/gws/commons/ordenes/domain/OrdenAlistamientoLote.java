package com.egakat.io.gws.commons.ordenes.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.egakat.core.data.jpa.domain.SimpleAuditableEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ordenes_alistamiento_lotes")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenAlistamientoLote extends SimpleAuditableEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	@Setter(value = AccessLevel.PROTECTED)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_orden_alistamiento_linea", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private OrdenAlistamientoLinea linea;

	@Column(name = "prtnum", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String prtnum;

	@Column(name = "invsts", length = 4, nullable = false)
	@NotNull
	@Size(max = 4)
	private String invsts;

	@Column(name = "untqty", nullable = false)
	private int untqty;

	@Column(name = "lotnum", length = 25, nullable = false)
	@NotNull
	@Size(max = 25)
	private String lotnum;

	@Column(name = "orgcod", length = 25, nullable = false)
	@NotNull
	@Size(max = 25)
	private String orgcod;

	@Column(name = "expire_dte")
	@DateTimeFormat(style = "M-")
	private LocalDateTime expireDte;

	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_estado_inventario", length = 4)
	private String idEstadoInventario;

}