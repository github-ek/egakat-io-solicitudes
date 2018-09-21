package com.egakat.io.gws.commons.ordenes.dto;

import java.util.ArrayList;
import java.util.List;

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
public class OrdenAlistamientoLineaDto extends SimpleAuditableEntityDto<Long> {

	private long idOrden;

	@NotNull
	@Size(max = 10)
	private String ordlin;
	
	@NotNull
	@Size(max = 50)
	private String prtnum;
	
	@NotNull
	@Size(max = 4)
	private String invsts;
	
	private int ordqty;
	
	private int stgqty;
	
	private int shpqty;
	
	private Integer numeroLinea;
	
	private Long idProducto;

	private String idEstadoInventario;
	
	private List<OrdenAlistamientoCancelacionDto> cancelaciones = new ArrayList<>();

	private List<OrdenAlistamientoLoteDto> lotes = new ArrayList<>();
}