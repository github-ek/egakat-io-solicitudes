package com.egakat.io.solicitudes.domain.salidas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.SolicitudTercero;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "salidas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Salida extends SolicitudTercero {

	public static final String PREDISTRIBUCION_CROSSDOCK = "PREDISTRIBUCION_CROSSDOCK";

	public static final String AUTORIZADO_IDENTIFICACION = "AUTORIZADO_IDENTIFICACION";
	public static final String AUTORIZADO_NOMBRES = "AUTORIZADO_NOMBRES";

	public static final String REQUIERE_RECAUDO = "REQUIERE_RECAUDO";
	public static final String VALOR_RECAUDAR = "VALOR_RECAUDAR";

	@Column(name = "predistribucion_crossdock", length = 200)
	@NotNull
	private String predistribucionCrossdock;

	@Column(name = "autorizado_identificacion", length = 20)
	@NotNull
	private String autorizadoIdentificacion;

	@Column(name = "autorizado_nombres", length = 100)
	@NotNull
	private String autorizadoNombres;

	@Column(name = "requiere_recaudo", length = 1)
	@NotNull
	private String requiereRecaudo;

	@Column(name = "valor_recaudar")
	private Integer valorRecaudar;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case PREDISTRIBUCION_CROSSDOCK:
			return getPredistribucionCrossdock();

		case AUTORIZADO_IDENTIFICACION:
			return getAutorizadoIdentificacion();
		case AUTORIZADO_NOMBRES:
			return getAutorizadoNombres();

		case REQUIERE_RECAUDO:
			return getRequiereRecaudo();
		case VALOR_RECAUDAR:
			return getValorRecaudar();
		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Builder
	public Salida(Long id, int version, LocalDateTime FechaCreacion, String createdBy, LocalDateTime FechaModificacion,
			String modifiedBy, Long idArchivo, EstadoRegistroType estado, int numeroLinea, String clienteCodigo,
			String servicioCodigoAlterno, String numeroSolicitud, String prefijo, String numeroSolicitudSinPrefijo,
			LocalDate femi, LocalDate fema, String nota, String productoCodigoAlterno, String productoNombre,
			int cantidad, String unidadMedidaCodigoAlterno, String bodegaCodigoAlterno,
			String estadoInventarioCodigoAlterno, String lote, Long idCliente, Long idServicio, Long idProducto,
			Long idUnidadMedida, Long idBodega, String idEstadoInventario, Long valorUnitarioDeclarado,
			String requiereAgendamiento, LocalTime homi, LocalTime homa, String terceroIdentificacion,
			String terceroNombre, String canalCodigoAlterno, String requiereTransporte, String ciudadCodigoAlterno,
			String direccion, String puntoCodigoAlterno, String puntoNombre, String contactoNombres,
			String contactoTelefonos, String contactoEmail, String documentoCliente, LocalDate fechaDocumentoCliente,
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto, String predistribucionCrossdock,
			String autorizadoIdentificacion, String autorizadoNombres, String requiereRecaudo, Integer valorRecaudar) {
		super(id, version, FechaCreacion, createdBy, FechaModificacion, modifiedBy, idArchivo, estado, numeroLinea,
				clienteCodigo, servicioCodigoAlterno, numeroSolicitud, prefijo, numeroSolicitudSinPrefijo, femi, fema,
				nota, productoCodigoAlterno, productoNombre, cantidad, unidadMedidaCodigoAlterno, bodegaCodigoAlterno,
				estadoInventarioCodigoAlterno, lote, idCliente, idServicio, idProducto, idUnidadMedida, idBodega,
				idEstadoInventario, valorUnitarioDeclarado, requiereAgendamiento, homi, homa, terceroIdentificacion,
				terceroNombre, canalCodigoAlterno, requiereTransporte, ciudadCodigoAlterno, direccion,
				puntoCodigoAlterno, puntoNombre, contactoNombres, contactoTelefonos, contactoEmail, documentoCliente,
				fechaDocumentoCliente, idTercero, idCanal, idCiudad, idPunto);
		this.predistribucionCrossdock = predistribucionCrossdock;
		this.autorizadoIdentificacion = autorizadoIdentificacion;
		this.autorizadoNombres = autorizadoNombres;
		this.requiereRecaudo = requiereRecaudo;
		this.valorRecaudar = valorRecaudar;
	}
}