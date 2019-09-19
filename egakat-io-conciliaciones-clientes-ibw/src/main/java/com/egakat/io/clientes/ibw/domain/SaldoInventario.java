package com.egakat.io.clientes.ibw.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.core.data.jpa.domain.AuditableEntityWithoutSequence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "saldos_inventario")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SaldoInventario extends AuditableEntityWithoutSequence<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	@Setter(value = AccessLevel.PROTECTED)
	private Long id;

	@Column(name = "id_archivo", updatable = false, nullable = false)
	@NotNull
	private Long idArchivo;

	@Column(name = "estado", length = 50)
	@NotNull
	private String estado;

	@Column(name = "numero_linea")
	int numeroLinea;

	@Column(name = "cliente_codigo", length = 20)
	@NotNull
	private String clienteCodigo;

	@Column(name = "fecha")
	@NotNull
	private LocalDate fecha;

	@Column(name = "fecha_corte")
	@NotNull
	private LocalDateTime fechaCorte;

	@Column(name = "producto_codigo_alterno", length = 50)
	@NotNull
	private String productoCodigoAlterno;

	@Column(name = "bodega_codigo_alterno", length = 50)
	@NotNull
	private String bodegaCodigoAlterno;

	@Column(name = "estado_conciliacion_codigo_alterno", length = 50)
	@NotNull
	private String estadoConciliacionCodigoAlterno;

	@Column(name = "unidad_medida_codigo_alterno", length = 50)
	@NotNull
	private String unidadMedidaCodigoAlterno;

	@Column(name = "unidades_por_empaque")
	private Integer unidadesPorEmpaque;

	@Column(name = "cantidad")
	@NotNull
	private BigDecimal cantidad;

	@Column(name = "valor_unitario")
	private Integer valorUnitario;

	@Column(name = "id_cliente")
	private Long idCliente;

	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_bodega")
	private Long idBodega;

	@Column(name = "id_estado_conciliacion")
	private Long idEstadoConciliacion;

	@Column(name = "id_unidad_medida")
	private Long idUnidadMedida;
}
