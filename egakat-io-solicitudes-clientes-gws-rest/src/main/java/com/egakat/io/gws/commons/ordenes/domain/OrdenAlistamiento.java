package com.egakat.io.gws.commons.ordenes.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.core.io.stage.domain.IntegrationEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ordenes_alistamiento")
@AttributeOverride(name = "id", column = @Column(name = "id_orden_alistamiento"))
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenAlistamiento extends IntegrationEntity {

	@Column(name = "client_id", length = 32, nullable = false)
	@NotNull
	@Size(max = 32)
	private String clientId;

	@Column(name = "wh_id", length = 32, nullable = false)
	@NotNull
	@Size(max = 32)
	private String whId;

	@Column(name = "ordnum", length = 35, nullable = false)
	@NotNull
	@Size(max = 35)
	private String ordnum;

	@Column(name = "ordtyp", length = 4, nullable = false)
	@NotNull
	@Size(max = 4)
	private String ordtyp;
	
	@Column(name = "id_cliente")
	private Long idCliente;

	@Column(name = "id_bodega")
	private Long idBodega;

	@Column(name = "id_orden")
	private Long idOrden;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orden", cascade = CascadeType.ALL)
	private List<OrdenAlistamientoLinea> lineas = new ArrayList<>();

	public void addLinea(OrdenAlistamientoLinea item) {
		lineas.add(item);
		item.setOrden(this);
	}

	public void removeLinea(OrdenAlistamientoLinea item) {
		lineas.remove(item);
		item.setOrden(null);
	}
}