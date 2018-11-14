package com.egakat.io.solicitudes.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.egakat.integration.files.domain.Registro;
import com.egakat.integration.files.enums.EstadoRegistroType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class Solicitud extends Registro {

	public static final String CLIENTE_CODIGO = "CLIENTE_CODIGO";
	public static final String SERVICIO_CODIGO_ALTERNO = "SERVICIO_CODIGO_ALTERNO";
	public static final String NUMERO_SOLICITUD = "NUMERO_SOLICITUD";
	public static final String PREFIJO = "PREFIJO";
	public static final String NUMERO_SOLICITUD_SIN_PREFIJO = "NUMERO_SOLICITUD_SIN_PREFIJO";

	public static final String FEMI = "FEMI";
	public static final String FEMA = "FEMA";
	
	public static final String NOTA = "NOTA";

	public static final String PRODUCTO_CODIGO_ALTERNO = "PRODUCTO_CODIGO_ALTERNO";
	public static final String PRODUCTO_NOMBRE = "PRODUCTO_NOMBRE";
	public static final String CANTIDAD = "CANTIDAD";
	public static final String UNIDAD_MEDIDA_CODIGO_ALTERNO = "UNIDAD_MEDIDA_CODIGO_ALTERNO";
	public static final String BODEGA_CODIGO_ALTERNO = "BODEGA_CODIGO_ALTERNO";
	public static final String ESTADO_INVENTARIO_CODIGO_ALTERNO = "ESTADO_INVENTARIO_CODIGO_ALTERNO";

	public static final String LOTE = "LOTE";

	@Column(name = "cliente_codigo", length = 20)
	@NotEmpty
	private String clienteCodigo;

	@Column(name = "servicio_codigo_alterno", length = 50)
	@NotEmpty
	private String servicioCodigoAlterno;

	@Column(name = "numero_solicitud", length = 20)
	@NotEmpty
	private String numeroSolicitud;

	@Column(name = "prefijo", length = 20)
	@NotNull
	private String prefijo;

	@Column(name = "numero_solicitud_sin_prefijo", length = 20)
	@NotNull
	private String numeroSolicitudSinPrefijo;

	@Column(name = "femi")
	private LocalDate femi;

	@Column(name = "fema")
	private LocalDate fema;

	@Column(name = "nota", length = 200)
	@NotNull
	private String nota;

	
	@Column(name = "producto_codigo_alterno", length = 50)
	@NotNull
	private String productoCodigoAlterno;

	@Column(name = "producto_nombre", length = 200)
	@NotNull
	private String productoNombre;

	@Column(name = "cantidad")
	private int cantidad;

	@Column(name = "unidad_medida_codigo_alterno", length = 50)
	@NotNull
	private String unidadMedidaCodigoAlterno;

	@Column(name = "bodega_codigo_alterno", length = 50)
	@NotEmpty
	private String bodegaCodigoAlterno;

	@Column(name = "estado_inventario_codigo_alterno", length = 50)
	@NotNull
	private String estadoInventarioCodigoAlterno;

	@Column(name = "lote", length = 30)
	@NotNull
	private String lote;

	@Column(name = "id_cliente")
	private Long idCliente;

	@Column(name = "id_servicio")
	private Long idServicio;

	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "id_unidad_medida")
	private Long idUnidadMedida;

	@Column(name = "id_bodega")
	private Long idBodega;

	@Column(name = "id_estado_inventario", length = 4)
	private String idEstadoInventario;

	@Override
	public String getIdCorrelacion() {
		return getNumeroSolicitud();
	}

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case CLIENTE_CODIGO:
		case SERVICIO_CODIGO_ALTERNO:
		case PRODUCTO_CODIGO_ALTERNO:
		case UNIDAD_MEDIDA_CODIGO_ALTERNO:
		case BODEGA_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_CODIGO_ALTERNO:
			return getStringValueFromHomologableProperty(property);

		case NUMERO_SOLICITUD:
			return getNumeroSolicitud();
		case PREFIJO:
			return getPrefijo();
		case NUMERO_SOLICITUD_SIN_PREFIJO:
			return getNumeroSolicitudSinPrefijo();
		case FEMI:
			return getFemi();
		case FEMA:
			return getFema();
		case NOTA:
			return getNota();
			
		case PRODUCTO_NOMBRE:
			return getProductoNombre();
		case CANTIDAD:
			return getCantidad();
		case LOTE:
			return getLote();
		default:
			return null;
		}
	}

	@Override
	public boolean isHomologableProperty(String property) {
		switch (property) {
		case CLIENTE_CODIGO:
		case SERVICIO_CODIGO_ALTERNO:
		case PRODUCTO_CODIGO_ALTERNO:
		case UNIDAD_MEDIDA_CODIGO_ALTERNO:
		case BODEGA_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_CODIGO_ALTERNO:
			return true;
		default:
			return false;
		}
	}

	@Override
	protected String getStringValueFromHomologableProperty(String property) {
		switch (property) {
		case CLIENTE_CODIGO:
			return getClienteCodigo();
		case SERVICIO_CODIGO_ALTERNO:
			return getServicioCodigoAlterno();
		case PRODUCTO_CODIGO_ALTERNO:
			return getProductoCodigoAlterno();
		case UNIDAD_MEDIDA_CODIGO_ALTERNO:
			return getUnidadMedidaCodigoAlterno();
		case BODEGA_CODIGO_ALTERNO:
			return getBodegaCodigoAlterno();
		case ESTADO_INVENTARIO_CODIGO_ALTERNO:
			return getEstadoInventarioCodigoAlterno();
		default:
			return null;
		}
	}

	@Override
	protected Object getObjectValueFromHomologousProperty(String property) {
		switch (property) {
		case CLIENTE_CODIGO:
			return getIdCliente();
		case SERVICIO_CODIGO_ALTERNO:
			return getIdServicio();
		case PRODUCTO_CODIGO_ALTERNO:
			return getIdProducto();
		case UNIDAD_MEDIDA_CODIGO_ALTERNO:
			return getIdUnidadMedida();
		case BODEGA_CODIGO_ALTERNO:
			return getIdBodega();
		case ESTADO_INVENTARIO_CODIGO_ALTERNO:
			return getIdEstadoInventario();
		default:
			return null;
		}
	}

	public Solicitud(Long id, int version, LocalDateTime fechaCreacion, String creadoPor,
			LocalDateTime fechaModificacion, String modificadoPor, Long idArchivo, @NotNull EstadoRegistroType estado,
			int numeroLinea, @NotEmpty String clienteCodigo, @NotEmpty String servicioCodigoAlterno,
			@NotEmpty String numeroSolicitud, @NotNull String prefijo, @NotNull String numeroSolicitudSinPrefijo,
			LocalDate femi, LocalDate fema, @NotNull String nota, @NotNull String productoCodigoAlterno,
			@NotNull String productoNombre, int cantidad, @NotNull String unidadMedidaCodigoAlterno,
			@NotEmpty String bodegaCodigoAlterno, @NotNull String estadoInventarioCodigoAlterno, @NotNull String lote,
			Long idCliente, Long idServicio, Long idProducto, Long idUnidadMedida, Long idBodega,
			String idEstadoInventario) {
		super(id, version, fechaCreacion, creadoPor, fechaModificacion, modificadoPor, idArchivo, estado, numeroLinea);
		this.clienteCodigo = clienteCodigo;
		this.servicioCodigoAlterno = servicioCodigoAlterno;
		this.numeroSolicitud = numeroSolicitud;
		this.prefijo = prefijo;
		this.numeroSolicitudSinPrefijo = numeroSolicitudSinPrefijo;
		this.femi = femi;
		this.fema = fema;
		this.nota = nota;
		this.productoCodigoAlterno = productoCodigoAlterno;
		this.productoNombre = productoNombre;
		this.cantidad = cantidad;
		this.unidadMedidaCodigoAlterno = unidadMedidaCodigoAlterno;
		this.bodegaCodigoAlterno = bodegaCodigoAlterno;
		this.estadoInventarioCodigoAlterno = estadoInventarioCodigoAlterno;
		this.lote = lote;
		this.idCliente = idCliente;
		this.idServicio = idServicio;
		this.idProducto = idProducto;
		this.idUnidadMedida = idUnidadMedida;
		this.idBodega = idBodega;
		this.idEstadoInventario = idEstadoInventario;
	}
}