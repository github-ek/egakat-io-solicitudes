package com.egakat.io.gws.commons.ordenes.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

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
public class OrdenAlistamientoLoteDto extends SimpleAuditableEntityDto<Long> {

	private long idLinea;

	@NotNull
	@Size(max = 50)
	private String prtnum;
	
	@NotNull
	@Size(max = 4)
	private String invsts;
	
	private int untqty;
	
	@NotNull
	@Size(max = 25)
	private String lotnum;
	
	@NotNull
	@Size(max = 25)
	private String orgcod;
	
	@DateTimeFormat(style = "M-")
	private LocalDateTime expireDte;
	
	
	private Long idProducto;

	private String idEstadoInventario;
}