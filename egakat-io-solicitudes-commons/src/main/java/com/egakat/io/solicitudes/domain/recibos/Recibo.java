package com.egakat.io.solicitudes.domain.recibos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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
@Table(name = "recibos")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Recibo extends SolicitudTercero {

	public static final String BL = "BL";
	public static final String CONTENEDOR = "CONTENEDOR";

	@Column(length = 50, nullable = false)
	@NotNull
	private String bl;

	@Column(length = 50, nullable = false)
	@NotNull
	private String contenedor;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case BL:
			return getBl();
		case CONTENEDOR:
			return getContenedor();
		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Builder
	public Recibo(Long id, int version, LocalDateTime fechaCreacion, String creadoPor, LocalDateTime fechaModificacion,
			String modificadoPor, Long idArchivo, @NotNull EstadoRegistroType estado, int numeroLinea,
			@NotEmpty String clienteCodigo, @NotEmpty String servicioCodigoAlterno, @NotEmpty String numeroSolicitud,
			@NotNull String prefijo, @NotNull String numeroSolicitudSinPrefijo, LocalDate femi, LocalDate fema,
			@NotNull String nota, @NotNull String productoCodigoAlterno, @NotNull String productoNombre, int cantidad,
			@NotNull String unidadMedidaCodigoAlterno, @NotEmpty String bodegaCodigoAlterno,
			@NotNull String estadoInventarioCodigoAlterno, @NotNull String lote, Long idCliente, Long idServicio,
			Long idProducto, Long idUnidadMedida, Long idBodega, String idEstadoInventario, Long valorUnitarioDeclarado,
			@NotNull String requiereAgendamiento, LocalTime homi, LocalTime homa, @NotNull String terceroIdentificacion,
			@NotNull String terceroNombre, @NotNull String canalCodigoAlterno, @NotEmpty String requiereTransporte,
			@NotNull String ciudadCodigoAlterno, @NotNull String direccion, @NotNull String puntoCodigoAlterno,
			@NotNull String puntoNombre, @NotNull String contactoNombres, @NotNull String contactoTelefonos,
			@Email String contactoEmail, @NotNull String documentoCliente, LocalDate fechaDocumentoCliente,
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto, @NotNull String bl, @NotNull String contenedor) {
		super(id, version, fechaCreacion, creadoPor, fechaModificacion, modificadoPor, idArchivo, estado, numeroLinea,
				clienteCodigo, servicioCodigoAlterno, numeroSolicitud, prefijo, numeroSolicitudSinPrefijo, femi, fema,
				nota, productoCodigoAlterno, productoNombre, cantidad, unidadMedidaCodigoAlterno, bodegaCodigoAlterno,
				estadoInventarioCodigoAlterno, lote, idCliente, idServicio, idProducto, idUnidadMedida, idBodega,
				idEstadoInventario, valorUnitarioDeclarado, requiereAgendamiento, homi, homa, terceroIdentificacion,
				terceroNombre, canalCodigoAlterno, requiereTransporte, ciudadCodigoAlterno, direccion,
				puntoCodigoAlterno, puntoNombre, contactoNombres, contactoTelefonos, contactoEmail, documentoCliente,
				fechaDocumentoCliente, idTercero, idCanal, idCiudad, idPunto);
		this.bl = bl;
		this.contenedor = contenedor;
	}
}