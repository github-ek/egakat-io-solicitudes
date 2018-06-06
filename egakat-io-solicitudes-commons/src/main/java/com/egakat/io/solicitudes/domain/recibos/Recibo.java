package com.egakat.io.solicitudes.domain.recibos;

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
@Table(name = "recibos")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Recibo extends SolicitudTercero {

	private static final long serialVersionUID = 1L;

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
	public Recibo(Long id, int version, LocalDateTime FechaCreacion, String createdBy, LocalDateTime FechaModificacion,
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
			Long idTercero, Long idCanal, Long idCiudad, Long idPunto, String bl, String contenedor) {
		super(id, version, FechaCreacion, createdBy, FechaModificacion, modifiedBy, idArchivo, estado, numeroLinea,
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