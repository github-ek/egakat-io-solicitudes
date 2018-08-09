package com.egakat.io.solicitudes.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.egakat.integration.files.enums.EstadoRegistroType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class SolicitudTercero extends Solicitud {

	
	public static final String VALOR_UNITARIO_DECLARADO = "VALOR_UNITARIO_DECLARADO";
	
	public static final String REQUIERE_AGENDAMIENTO = "REQUIERE_AGENDAMIENTO";
	public static final String HOMI = "HOMI";
	public static final String HOMA = "HOMA";

	public static final String TERCERO_IDENTIFICACION = "TERCERO_IDENTIFICACION";
	public static final String TERCERO_NOMBRE = "TERCERO_NOMBRE";
	public static final String CANAL_CODIGO_ALTERNO = "CANAL_CODIGO_ALTERNO";

	public static final String REQUIERE_TRANSPORTE = "REQUIERE_TRANSPORTE";
	public static final String CIUDAD_CODIGO_ALTERNO = "CIUDAD_CODIGO_ALTERNO";
	public static final String DIRECCION = "DIRECCION";
	public static final String PUNTO_CODIGO_ALTERNO = "PUNTO_CODIGO_ALTERNO";
	public static final String PUNTO_NOMBRE = "PUNTO_NOMBRE";

	public static final String CONTACTO_NOMBRES = "CONTACTO_NOMBRES";
	public static final String CONTACTO_TELEFONOS = "CONTACTO_TELEFONOS";
	public static final String CONTACTO_EMAIL = "CONTACTO_EMAIL";

	public static final String DOCUMENTO_CLIENTE = "DOCUMENTO_CLIENTE";
	public static final String FECHA_DOCUMENTO_CLIENTE = "FECHA_DOCUMENTO_CLIENTE";

	@Column(name = "valor_unitario_declarado")
	private Long valorUnitarioDeclarado;
	

	@Column(name = "requiere_agendamiento", length = 1)
	@NotNull
	private String requiereAgendamiento;

	@Column(name = "homi")
	private LocalTime homi;

	@Column(name = "homa")
	private LocalTime homa;

	
	@Column(name = "tercero_identificacion", length = 20)
	@NotNull
	private String terceroIdentificacion;

	@Column(name = "tercero_nombre", length = 100)
	@NotNull
	private String terceroNombre;

	@Column(name = "canal_codigo_alterno", length = 50)
	@NotNull
	private String canalCodigoAlterno;

	
	@Column(name = "requiere_transporte", length = 1)
	@NotEmpty
	private String requiereTransporte;

	@Column(name = "ciudad_codigo_alterno", length = 50)
	@NotNull
	private String ciudadCodigoAlterno;

	@Column(name = "direccion", length = 150)
	@NotNull
	private String direccion;

	@Column(name = "punto_codigo_alterno", length = 50)
	@NotNull
	private String puntoCodigoAlterno;

	@Column(name = "punto_nombre", length = 100)
	@NotNull
	private String puntoNombre;

	
	@Column(name = "contacto_nombres", length = 100)
	@NotNull
	private String contactoNombres;

	@Column(name = "contacto_telefonos", length = 50)
	@NotNull
	private String contactoTelefonos;

	@Column(name = "contacto_email", length = 100)
	@Email
	private String contactoEmail;

	
	@Column(name = "documento_cliente", length = 20)
	@NotNull
	private String documentoCliente;

	@Column(name = "fecha_documento_cliente")
	private LocalDate fechaDocumentoCliente;


	
	@Column(name = "id_tercero")
	private Long idTercero;

	@Column(name = "id_canal")
	private Long idCanal;

	@Column(name = "id_ciudad")
	private Long idCiudad;

	@Column(name = "id_punto")
	private Long idPunto;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case TERCERO_IDENTIFICACION:
		case CANAL_CODIGO_ALTERNO:
		case CIUDAD_CODIGO_ALTERNO:
		case PUNTO_CODIGO_ALTERNO:
			return getStringValueFromHomologableProperty(property);

		case REQUIERE_AGENDAMIENTO:
			return getRequiereAgendamiento();
		case HOMI:
			return getHomi();
		case HOMA:
			return getHoma();

		case TERCERO_NOMBRE:
			return getTerceroNombre();

		case REQUIERE_TRANSPORTE:
			return getRequiereTransporte();
		case DIRECCION:
			return getDireccion();
		case PUNTO_NOMBRE:
			return getPuntoNombre();

		case CONTACTO_NOMBRES:
			return getContactoNombres();
		case CONTACTO_TELEFONOS:
			return getContactoTelefonos();
		case CONTACTO_EMAIL:
			return getContactoEmail();

		case DOCUMENTO_CLIENTE:
			return getDocumentoCliente();
		case FECHA_DOCUMENTO_CLIENTE:
			return getFechaDocumentoCliente();

		case VALOR_UNITARIO_DECLARADO:
			return getValorUnitarioDeclarado();
			
		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Override
	public boolean isHomologableProperty(String property) {
		switch (property) {
		case TERCERO_IDENTIFICACION:
		case CANAL_CODIGO_ALTERNO:
		case CIUDAD_CODIGO_ALTERNO:
		case PUNTO_CODIGO_ALTERNO:
			return true;
		default:
			return super.isHomologableProperty(property);
		}
	}

	@Override
	protected String getStringValueFromHomologableProperty(String property) {
		switch (property) {
		case TERCERO_IDENTIFICACION:
			return getTerceroIdentificacion();
		case CANAL_CODIGO_ALTERNO:
			return getCanalCodigoAlterno();
		case CIUDAD_CODIGO_ALTERNO:
			return getCiudadCodigoAlterno();
		case PUNTO_CODIGO_ALTERNO:
			return getPuntoCodigoAlterno();
		default:
			return super.getStringValueFromHomologableProperty(property);
		}
	}

	@Override
	protected Object getObjectValueFromHomologousProperty(String property) {
		switch (property) {
		case TERCERO_IDENTIFICACION:
			return getIdTercero();
		case CANAL_CODIGO_ALTERNO:
			return getIdCanal();
		case CIUDAD_CODIGO_ALTERNO:
			return getIdCiudad();
		case PUNTO_CODIGO_ALTERNO:
			return getIdPunto();
		default:
			return super.getObjectValueFromHomologousProperty(property);
		}
	}

	public SolicitudTercero(Long id, int version, LocalDateTime FechaCreacion, String createdBy,
			LocalDateTime FechaModificacion, String modifiedBy, Long idArchivo, EstadoRegistroType estado, int numeroLinea,
			String clienteCodigo, String servicioCodigoAlterno, String numeroSolicitud, String prefijo,
			String numeroSolicitudSinPrefijo, LocalDate femi, LocalDate fema, String nota, String productoCodigoAlterno,
			String productoNombre, int cantidad, String unidadMedidaCodigoAlterno, String bodegaCodigoAlterno,
			String estadoInventarioCodigoAlterno, String lote, Long idCliente, Long idServicio, Long idProducto,
			Long idUnidadMedida, Long idBodega, String idEstadoInventario, Long valorUnitarioDeclarado,
			String requiereAgendamiento, LocalTime homi, LocalTime homa, String terceroIdentificacion,
			String terceroNombre, String canalCodigoAlterno, String requiereTransporte, String ciudadCodigoAlterno,
			String direccion, String puntoCodigoAlterno, String puntoNombre, String contactoNombres,
			String contactoTelefonos, String contactoEmail, String documentoCliente, LocalDate fechaDocumentoCliente,
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto) {
		super(id, version, FechaCreacion, createdBy, FechaModificacion, modifiedBy, idArchivo, estado, numeroLinea,
				clienteCodigo, servicioCodigoAlterno, numeroSolicitud, prefijo, numeroSolicitudSinPrefijo, femi, fema,
				nota, productoCodigoAlterno, productoNombre, cantidad, unidadMedidaCodigoAlterno, bodegaCodigoAlterno,
				estadoInventarioCodigoAlterno, lote, idCliente, idServicio, idProducto, idUnidadMedida, idBodega,
				idEstadoInventario);
		this.valorUnitarioDeclarado = valorUnitarioDeclarado;
		this.requiereAgendamiento = requiereAgendamiento;
		this.homi = homi;
		this.homa = homa;
		this.terceroIdentificacion = terceroIdentificacion;
		this.terceroNombre = terceroNombre;
		this.canalCodigoAlterno = canalCodigoAlterno;
		this.requiereTransporte = requiereTransporte;
		this.ciudadCodigoAlterno = ciudadCodigoAlterno;
		this.direccion = direccion;
		this.puntoCodigoAlterno = puntoCodigoAlterno;
		this.puntoNombre = puntoNombre;
		this.contactoNombres = contactoNombres;
		this.contactoTelefonos = contactoTelefonos;
		this.contactoEmail = contactoEmail;
		this.documentoCliente = documentoCliente;
		this.fechaDocumentoCliente = fechaDocumentoCliente;
		this.idTercero = idTercero;
		this.idCanal = idCanal;
		this.idCiudad = idCiudad;
		this.idPunto = idPunto;
	}
}