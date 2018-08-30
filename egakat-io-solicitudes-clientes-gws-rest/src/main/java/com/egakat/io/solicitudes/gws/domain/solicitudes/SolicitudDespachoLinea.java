package com.egakat.io.solicitudes.gws.domain.solicitudes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.NumberFormat;

import com.egakat.commons.domain.SimpleEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solicitudes_despacho_lineas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SolicitudDespachoLinea extends SimpleEntity<Long> {

	@Column(name = "id_solicitud_despacho")
    @NumberFormat
    private long idSolicitudDespacho;
    
	@Column(name = "numero_linea")
	private int numeroLinea;
    
	@Column(name = "numero_linea_externo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String numeroLineaExterno;

	@Column(name = "numero_sublinea_externo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String numeroSubLineaExterno;

	@Column(name = "producto_codigo_alterno", length = 50)
	@NotEmpty
	@Size(max = 50)
	private String productoCodigoAlterno;

	@Column(name = "producto_nombre", length = 200)
	@NotNull
	@Size(max = 200)
	private String productoNombre;

	@Column(name = "cantidad")
	private int cantidad;

	@Column(name = "bodega_codigo_alterno", length = 50)
	@NotEmpty
	@Size(max = 50)
	private String bodegaCodigoAlterno;

	@Column(name = "estado_inventario_codigo_alterno", length = 50)
	@NotEmpty
	@Size(max = 50)
	private String estadoInventarioCodigoAlterno;

	@Column(name = "lote", length = 30)
	@NotNull
	@Size(max = 30)
	private String lote;

	@Column(name = "predistribucion", length = 200)
	@NotNull
	@Size(max = 200)
	private String predistribucion;
	
	@Column(name = "valor_unitario_declarado")
	private Integer valorUnitarioDeclarado;
	
	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_bodega")
	private Long idBodega;

	@Column(name = "id_estado_inventario", length = 4)
	private String idEstadoInventario;

}