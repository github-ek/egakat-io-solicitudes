package com.egakat.io.solicitudes.gws.dto.solicitudes;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.dto.SimpleEntityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDespachoLineaDto extends SimpleEntityDto<Long> {

	private long idSolicitudDespacho;
	
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

	@Builder
	public SolicitudDespachoLineaDto(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			long idSolicitudDespacho, int numeroLinea, @NotNull @Size(max = 50) String numeroLineaExterno,
			@NotNull @Size(max = 50) String numeroSubLineaExterno,
			@NotNull @Size(max = 50) String productoCodigoAlterno, @NotNull @Size(max = 200) String productoNombre,
			int cantidad, @NotNull @Size(max = 50) String bodegaCodigoAlterno,
			@NotNull @Size(max = 50) String estadoInventarioCodigoAlterno, @NotNull @Size(max = 30) String lote,
			@NotNull @Size(max = 200) String predistribucion, Integer valorUnitarioDeclarado, Long idProducto,
			Long idBodega, @Size(max = 4) String idEstadoInventario) {
		super(id, version, fechaCreacion, fechaModificacion);
		this.idSolicitudDespacho = idSolicitudDespacho;
		this.numeroLinea = numeroLinea;
		this.numeroLineaExterno = numeroLineaExterno;
		this.numeroSubLineaExterno = numeroSubLineaExterno;
		this.productoCodigoAlterno = productoCodigoAlterno;
		this.productoNombre = productoNombre;
		this.cantidad = cantidad;
		this.bodegaCodigoAlterno = bodegaCodigoAlterno;
		this.estadoInventarioCodigoAlterno = estadoInventarioCodigoAlterno;
		this.lote = lote;
		this.predistribucion = predistribucion;
		this.valorUnitarioDeclarado = valorUnitarioDeclarado;
		this.idProducto = idProducto;
		this.idBodega = idBodega;
		this.idEstadoInventario = idEstadoInventario;
	}
}