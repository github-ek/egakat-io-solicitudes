package com.egakat.io.commons.solicitudes.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.integration.domain.IntegracionEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solicitudes_despacho")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_despacho"))
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SolicitudDespacho extends IntegracionEntity {

	@Column(name = "cliente_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String clienteCodigoAlterno;

	@Column(name = "servicio_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String servicioCodigoAlterno;

	@Column(name = "numero_solicitud", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroSolicitud;

	@Column(name = "prefijo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String prefijo;

	@Column(name = "numero_solicitud_sin_prefijo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroSolicitudSinPrefijo;

	@Column(name = "femi")
	private LocalDate femi;

	@Column(name = "fema")
	private LocalDate fema;

	@Column(name = "homi")
	private LocalTime homi;

	@Column(name = "homa")
	private LocalTime homa;

	@Column(name = "requiere_transporte", nullable = false)
	private boolean requiereTransporte;

	@Column(name = "requiere_agendamiento", nullable = false)
	private boolean requiereAgendamiento;

	@Column(name = "requiere_despachar_completo", nullable = false)
	private boolean requiereDespacharCompleto;

	@Column(name = "tercero_identificacion", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String terceroIdentificacion;

	@Column(name = "tercero_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String terceroNombre;

	@Column(name = "canal_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String canalCodigoAlterno;

	@Column(name = "ciudad_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String ciudadCodigoAlterno;

	@Column(name = "direccion", length = 150, nullable = false)
	@NotNull
	@Size(max = 150)
	private String direccion;

	@Column(name = "punto_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String puntoCodigoAlterno;

	@Column(name = "punto_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String puntoNombre;

	@Column(name = "contacto_principal_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalNombre;
	
	@Column(name = "contacto_principal_telefono", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalTelefono;

	@Column(name = "contacto_secundario_nombre", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String contactoSecundarioNombre;
	
	@Column(name = "contacto_secundario_telefono", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String contactoSecundarioTelefono;
	
	@Column(name = "autorizado_identificacion", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String autorizadoIdentificacion;

	@Column(name = "autorizado_nombres", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String autorizadoNombres;
	
	@Column(name = "tipo_documento_codigo_orden_compra", length = 50, nullable = true)
	@Size(max = 50)
	private String tipoDocumentoCodigoOrdenCompra;

	@Column(name = "numero_orden_compra", length = 20, nullable = true)
	@Size(max = 20)
	private String numeroOrdenCompra;

	@Column(name = "fecha_orden_compra")
	private LocalDate fechaOrdenCompra;

	@Column(name = "tipo_documento_codigo_aux1", length = 50, nullable = true)
	@Size(max = 50)
	private String tipoDocumentoCodigoAuxiliar1;

	@Column(name = "numero_documento_aux1", length = 50, nullable = true)
	@Size(max = 50)
	private String numeroDocumentoAuxiliar1;

	@Column(name = "fecha_documento_aux1")
	private LocalDate fechaDocumentoAuxiliar1;

	@Column(name = "tipo_documento_codigo_aux2", length = 50, nullable = true)
	@Size(max = 50)
	private String tipoDocumentoCodigoAuxiliar2;

	@Column(name = "numero_documento_aux2", length = 50, nullable = true)
	@Size(max = 50)
	private String numeroDocumentoAuxiliar2;

	@Column(name = "fecha_documento_aux2")
	private LocalDate fechaDocumentoAuxiliar2;
	
	@Column(name = "nota", length = 200, nullable = false)
	@NotNull
	@Size(max = 200)
	private String nota;

	@Column(name = "id_cliente")
	private Long idCliente;

	@Column(name = "id_servicio")
	private Long idServicio;

	@Column(name = "id_tercero")
	private Long idTercero;

	@Column(name = "id_canal")
	private Long idCanal;

	@Column(name = "id_ciudad")
	private Long idCiudad;

	@Column(name = "id_punto")
	private Long idPunto;

	@Column(name = "fecha_creacion_externa")
	private LocalDateTime fechaCreacionExterna;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitud", cascade = CascadeType.ALL)
	private List<SolicitudDespachoLinea> lineas = new ArrayList<>();

	public void add(SolicitudDespachoLinea item) {
		lineas.add(item);
		item.setSolicitud(this);
	}

	public void remove(SolicitudDespachoLinea item) {
		lineas.remove(item);
		item.setSolicitud(null);
	}
}