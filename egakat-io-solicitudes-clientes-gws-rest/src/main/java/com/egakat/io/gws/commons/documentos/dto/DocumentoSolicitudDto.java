package com.egakat.io.gws.commons.documentos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.core.io.stage.dto.IntegrationEntityDto;

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
public class DocumentoSolicitudDto extends IntegrationEntityDto {

	@NotNull
	@Size(max = 50)
	private String clienteCodigoAlterno;
	
	@NotNull
	@Size(max = 50)
	private String tipoDocumentoCodigoAlterno;
	
	@NotNull
	@Size(max = 20)
	private String numeroSolicitud;
	
	@NotNull
	@Size(max = 20)
	private String prefijo;
	
	@NotNull
	@Size(max = 20)
	private String numeroSolicitudSinPrefijo;
	
	@NotNull
	@Size(max = 20)
	private String numeroDocumento;

	@NotNull
	@Size(max = 20)
	private String prefijoDocumento;
	
	@NotNull
	@Size(max = 20)
	private String numeroDocumentoSinPrefijo;

	private Long idCliente;

	private Long idTipoDocumento;

	private Long idSolicitud;

	private List<DocumentoSolicitudLineaDto> lineas = new ArrayList<>();
}