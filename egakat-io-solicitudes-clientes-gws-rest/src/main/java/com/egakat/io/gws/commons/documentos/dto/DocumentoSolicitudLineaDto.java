package com.egakat.io.gws.commons.documentos.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.dto.SimpleAuditableEntityDto;

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
public class DocumentoSolicitudLineaDto extends SimpleAuditableEntityDto<Long> {

	private int numeroLinea;
    
	@NotNull
	@Size(max = 50)
	private String numeroLineaExterno;

	@NotNull
	@Size(max = 50)
	private String numeroSubLineaExterno;

	@NotEmpty
	@Size(max = 50)
	private String productoCodigoAlterno;

	@NotNull
	@Size(max = 200)
	private String productoNombre;

	private int cantidad;

	@NotEmpty
	@Size(max = 50)
	private String bodegaCodigoAlterno;

	@NotEmpty
	@Size(max = 50)
	private String estadoInventarioCodigoAlterno;

	@NotNull
	@Size(max = 30)
	private String lote;
	
	private Long idProducto;

	private Long idBodega;

	private String idEstadoInventario;
}