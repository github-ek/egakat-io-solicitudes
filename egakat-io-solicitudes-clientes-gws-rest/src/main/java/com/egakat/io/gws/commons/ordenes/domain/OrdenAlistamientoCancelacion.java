package com.egakat.io.gws.commons.ordenes.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.egakat.commons.domain.SimpleAuditableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ordenes_alistamiento_cancelaciones")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenAlistamientoCancelacion extends SimpleAuditableEntity<Long> {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_orden_alistamiento_linea", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private OrdenAlistamientoLinea linea;


	@Column(name = "prtnum", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String prtnum;
	
	@Column(name = "cancod", length = 40, nullable = false)
	@NotNull
	@Size(max = 40)
	private String cancod;
	
	@Column(name = "lngdsc", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String lngdsc;
	
	@Column(name = "remqty", nullable = false)
	private int remqty;
	
	@Column(name = "can_usr_id", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String canUsrId;
	
	@Column(name = "candte")
	@DateTimeFormat(style = "M-")
	private LocalDateTime canDte;
	
	
	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_causal_cancelacion", length = 4)
	private String idCausalCancelacion;

}
