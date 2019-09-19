package com.egakat.io.commons.solicitudes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.integration.dto.IntegracionEntityDto;

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
public abstract class AbstractSolicitudDespachoDto<L extends SolicitudDespachoLineaDto> extends IntegracionEntityDto {

	@NotNull
	@Size(max = 50)
	private String clienteCodigoAlterno;
	
	@NotNull
	@Size(max = 50)
	private String servicioCodigoAlterno;
	
	@NotNull
	@Size(max = 20)
	private String numeroSolicitud;
	
	@NotNull
	@Size(max = 20)
	private String prefijo;
	
	@NotNull
	@Size(max = 20)
	private String numeroSolicitudSinPrefijo;
	
	private LocalDate femi;
	private LocalDate fema;
	private LocalTime homi;
	private LocalTime homa;
	
	private boolean requiereTransporte;
	private boolean requiereAgendamiento;
	private boolean requiereDespacharCompleto;
	
	@NotNull
	@Size(max = 20)
	private String terceroIdentificacion;
	
	@NotNull
	@Size(max = 100)
	private String terceroNombre;

	@NotNull
	@Size(max = 50)
	private String canalCodigoAlterno;
	
	@NotNull
	@Size(max = 50)
	private String ciudadCodigoAlterno;
	
	@NotNull
	@Size(max = 150)
	private String direccion;
	
	@NotNull
	@Size(max = 50)
	private String puntoCodigoAlterno;
	
	@NotNull
	@Size(max = 100)
	private String puntoNombre;
	
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalNombre;
	
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalTelefono;
	
	@NotNull
	@Size(max = 100)
	private String contactoSecundarioNombre;
	
	@NotNull
	@Size(max = 100)
	private String contactoSecundarioTelefono;
	
	@NotNull
	@Size(max = 100)
	private String autorizadoIdentificacion;
	
	@NotNull
	@Size(max = 100)
	private String autorizadoNombres;
	
	@Size(max = 50)
	private String tipoDocumentoCodigoOrdenCompra;
	
	@Size(max = 20)
	private String numeroOrdenCompra;
	
	private LocalDate fechaOrdenCompra;

	@Size(max = 50)
	private String tipoDocumentoCodigoAuxiliar1;
	
	@Size(max = 50)
	private String numeroDocumentoAuxiliar1;
	
	private LocalDate fechaDocumentoAuxiliar1;

	@Size(max = 50)
	private String tipoDocumentoCodigoAuxiliar2;

	@Size(max = 50)
	private String numeroDocumentoAuxiliar2;
	
	private LocalDate fechaDocumentoAuxiliar2;
	
	@NotNull
	@Size(max = 200)
	private String nota;
	
	private Long idCliente;
	private Long idServicio;
	private Long idTercero;
	private Long idCanal;
	private Long idCiudad;
	private Long idPunto;
	
	private LocalDateTime fechaCreacionExterna;
	
	private List<L> lineas;
}