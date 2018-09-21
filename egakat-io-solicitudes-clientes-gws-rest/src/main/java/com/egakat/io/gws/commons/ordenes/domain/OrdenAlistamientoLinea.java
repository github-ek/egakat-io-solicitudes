package com.egakat.io.gws.commons.ordenes.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.egakat.commons.domain.SimpleAuditableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ordenes_alistamiento_lineas")
@AttributeOverride(name = "id", column = @Column(name = "id_orden_alistamiento_linea"))
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenAlistamientoLinea extends SimpleAuditableEntity<Long> {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_orden_alistamiento", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private OrdenAlistamiento orden;

	@Column(name = "ordlin", length = 10, nullable = false)
	@NotNull
	@Size(max = 10)
	private String ordlin;
	
	@Column(name = "prtnum", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String prtnum;
	
	@Column(name = "invsts", length = 4, nullable = false)
	@NotNull
	@Size(max = 4)
	private String invsts;
	
	@Column(name = "ordqty", nullable = false)
	private int ordqty;
	
	@Column(name = "stgqty", nullable = false)
	private int stgqty;
	
	@Column(name = "shpqty", nullable = false)
	private int shpqty;
	
	@Column(name = "numero_linea", nullable = false)
	private Integer numeroLinea;
	
	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_estado_inventario", length = 4)
	private String idEstadoInventario;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "linea", cascade = CascadeType.ALL)
	private List<OrdenAlistamientoCancelacion> cancelaciones = new ArrayList<>();

	public void add(OrdenAlistamientoCancelacion item) {
		cancelaciones.add(item);
		item.setLinea(this);
	}

	public void remove(OrdenAlistamientoCancelacion item) {
		cancelaciones.remove(item);
		item.setLinea(null);
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "linea", cascade = CascadeType.ALL)
	private List<OrdenAlistamientoLote> lotes = new ArrayList<>();

	public void add(OrdenAlistamientoLote item) {
		lotes.add(item);
		item.setLinea(this);
	}

	public void remove(OrdenAlistamientoLote item) {
		lotes.remove(item);
		item.setLinea(null);
	}	
}