package com.egakat.io.commons.solicitudes.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.dto.SimpleAuditableEntityDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDespachoLineaDto extends SimpleAuditableEntityDto<Long> {
	
	private int numeroLinea;

	@NotNull
	@Size(max = 50)
	private String numeroLineaExterno;

	@NotNull
	@Size(max = 50)
	private String numeroSubLineaExterno;

	@NotNull
	@Size(max = 50)
	private String productoCodigoAlterno;

	@NotNull
	@Size(max = 200)
	private String productoNombre;

	private int cantidad;

	@NotNull
	@Size(max = 50)
	private String bodegaCodigoAlterno;

	@NotNull
	@Size(max = 50)
	private String estadoInventarioCodigoAlterno;

	@NotNull
	@Size(max = 30)
	private String lote;

	@NotNull
	@Size(max = 200)
	private String predistribucion;

	private Integer valorUnitarioDeclarado;

	private Long idProducto;

	private Long idBodega;

	@Size(max = 4)
	private String idEstadoInventario;
}