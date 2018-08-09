package com.egakat.io.solicitudes.gws.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.commons.dto.BusinessEntityDto;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;

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
public class SolicitudDespachoDto extends BusinessEntityDto<Long> {

	@NotNull
	private EstadoEntradaIntegracionType estado;

	@NotNull
	@Size(max = 50)
	private String integracion;

	@NotNull
	@Size(max = 100)
	private String idExterno;

	@NotNull
	@Size(max = 100)
	private String idCorrelacion;

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

	@NotNull
	@Size(max = 1)
	private String requiereAgendamiento;

	@NotNull
	@Size(max = 1)
	private String requiereTransporte;

	@NotNull
	@Size(max = 1)
	private String requiereDespacharCompleto;

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

	@NotNull
	private LocalDateTime fechaCreacionExterna;

	@Builder
	public SolicitudDespachoDto(Long id, int version, String creadoPor, LocalDateTime fechaCreacion,
			String modificadoPor, LocalDateTime fechaModificacion, @NotNull EstadoEntradaIntegracionType estado,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String idExterno,
			@NotNull @Size(max = 100) String idCorrelacion, @NotNull @Size(max = 50) String clienteCodigoAlterno,
			@NotNull @Size(max = 50) String servicioCodigoAlterno, @NotNull @Size(max = 20) String numeroSolicitud,
			@NotNull @Size(max = 20) String prefijo, @NotNull @Size(max = 20) String numeroSolicitudSinPrefijo,
			LocalDate femi, LocalDate fema, LocalTime homi, LocalTime homa,
			@NotNull @Size(max = 1) String requiereAgendamiento, @NotNull @Size(max = 1) String requiereTransporte,
			@NotNull @Size(max = 1) String requiereDespacharCompleto,
			@NotNull @Size(max = 20) String terceroIdentificacion, @NotNull @Size(max = 100) String terceroNombre,
			@NotNull @Size(max = 50) String canalCodigoAlterno, @NotNull @Size(max = 50) String ciudadCodigoAlterno,
			@NotNull @Size(max = 150) String direccion, @NotNull @Size(max = 50) String puntoCodigoAlterno,
			@NotNull @Size(max = 100) String puntoNombre, @NotNull @Size(max = 20) String autorizadoIdentificacion,
			@NotNull @Size(max = 100) String autorizadoNombres, @NotNull @Size(max = 20) String numeroOrdenCompra,
			LocalDate fechaOrdenCompra, @NotNull @Size(max = 200) String nota, Long idCliente, Long idServicio,
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto, @NotNull LocalDateTime fechaCreacionExterna) {
		super(id, version, fechaCreacion, creadoPor, fechaModificacion, modificadoPor);
		this.estado = estado;
		this.integracion = integracion;
		this.idExterno = idExterno;
		this.idCorrelacion = idCorrelacion;
		this.clienteCodigoAlterno = clienteCodigoAlterno;
		this.servicioCodigoAlterno = servicioCodigoAlterno;
		this.numeroSolicitud = numeroSolicitud;
		this.prefijo = prefijo;
		this.numeroSolicitudSinPrefijo = numeroSolicitudSinPrefijo;
		this.femi = femi;
		this.fema = fema;
		this.homi = homi;
		this.homa = homa;
		this.requiereAgendamiento = requiereAgendamiento;
		this.requiereTransporte = requiereTransporte;
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
	}
}