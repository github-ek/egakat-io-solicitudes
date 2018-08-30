package com.egakat.io.solicitudes.gws.dto.solicitudes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.io.solicitudes.gws.dto.DataQualityEntityDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

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
public class SolicitudDespachoDto extends DataQualityEntityDto {

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
	@Size(max = 20)
	private String autorizadoIdentificacion;
	
	@NotNull
	@Size(max = 100)
	private String autorizadoNombres;
	
	
	@NotNull
	@Size(max = 20)
	private String numeroOrdenCompra;
	private LocalDate fechaOrdenCompra;
	
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
	
	private List<SolicitudDespachoLineaDto> lineas;

	@Builder
	public SolicitudDespachoDto(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String correlacion,
			@NotNull @Size(max = 100) String idExterno, @NotNull EstadoIntegracionType estadoIntegracion,
			@NotNull @Size(max = 50) String clienteCodigoAlterno, @NotNull @Size(max = 50) String servicioCodigoAlterno,
			@NotNull @Size(max = 20) String numeroSolicitud, @NotNull @Size(max = 20) String prefijo,
			@NotNull @Size(max = 20) String numeroSolicitudSinPrefijo, LocalDate femi, LocalDate fema, LocalTime homi,
			LocalTime homa, boolean requiereTransporte, boolean requiereAgendamiento, boolean requiereDespacharCompleto,
			@NotNull @Size(max = 20) String terceroIdentificacion, @NotNull @Size(max = 100) String terceroNombre,
			@NotNull @Size(max = 50) String canalCodigoAlterno, @NotNull @Size(max = 50) String ciudadCodigoAlterno,
			@NotNull @Size(max = 150) String direccion, @NotNull @Size(max = 50) String puntoCodigoAlterno,
			@NotNull @Size(max = 100) String puntoNombre, @NotNull @Size(max = 20) String autorizadoIdentificacion,
			@NotNull @Size(max = 100) String autorizadoNombres, @NotNull @Size(max = 20) String numeroOrdenCompra,
			LocalDate fechaOrdenCompra, @NotNull @Size(max = 200) String nota, Long idCliente, Long idServicio,
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto, LocalDateTime fechaCreacionExterna,
			List<SolicitudDespachoLineaDto> lineas) {
		super(id, version, fechaCreacion, fechaModificacion, integracion, correlacion, idExterno, estadoIntegracion);
		this.clienteCodigoAlterno = clienteCodigoAlterno;
		this.servicioCodigoAlterno = servicioCodigoAlterno;
		this.numeroSolicitud = numeroSolicitud;
		this.prefijo = prefijo;
		this.numeroSolicitudSinPrefijo = numeroSolicitudSinPrefijo;
		this.femi = femi;
		this.fema = fema;
		this.homi = homi;
		this.homa = homa;
		this.requiereTransporte = requiereTransporte;
		this.requiereAgendamiento = requiereAgendamiento;
		this.requiereDespacharCompleto = requiereDespacharCompleto;
		this.terceroIdentificacion = terceroIdentificacion;
		this.terceroNombre = terceroNombre;
		this.canalCodigoAlterno = canalCodigoAlterno;
		this.ciudadCodigoAlterno = ciudadCodigoAlterno;
		this.direccion = direccion;
		this.puntoCodigoAlterno = puntoCodigoAlterno;
		this.puntoNombre = puntoNombre;
		this.autorizadoIdentificacion = autorizadoIdentificacion;
		this.autorizadoNombres = autorizadoNombres;
		this.numeroOrdenCompra = numeroOrdenCompra;
		this.fechaOrdenCompra = fechaOrdenCompra;
		this.nota = nota;
		this.idCliente = idCliente;
		this.idServicio = idServicio;
		this.idTercero = idTercero;
		this.idCanal = idCanal;
		this.idCiudad = idCiudad;
		this.idPunto = idPunto;
		this.fechaCreacionExterna = fechaCreacionExterna;
		this.lineas = lineas;
	}
}