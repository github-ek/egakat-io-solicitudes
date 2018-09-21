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
public class OrdenAlistamientoCancelacionDto extends SimpleAuditableEntityDto<Long> {

	private long idLinea;

	@NotNull
	@Size(max = 50)
	private String prtnum;

	@NotNull
	@Size(max = 40)
	private String cancod;

	@NotNull
	@Size(max = 100)
	private String lngdsc;

	private int remqty;

	@NotNull
	@Size(max = 50)
	private String canUsrId;

	@DateTimeFormat(style = "M-")
	private LocalDateTime canDte;

	private Long idProducto;

	private String idCausalCancelacion;

}