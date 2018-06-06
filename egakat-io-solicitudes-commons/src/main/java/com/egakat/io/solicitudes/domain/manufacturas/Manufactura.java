package com.egakat.io.solicitudes.domain.manufacturas;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.Solicitud;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "manufacturas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Manufactura extends Solicitud {

	public static final String REQUIERE_BOM = "REQUIERE_BOM";

	private static final long serialVersionUID = 1L;

	@Column(name = "requiere_bom")
	private boolean requiereBom;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case REQUIERE_BOM:
			return isRequiereBom();

		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Builder
	public Manufactura(Long id, int version, LocalDateTime FechaCreacion, String createdBy,
			LocalDateTime FechaModificacion, String modifiedBy, Long idArchivo, EstadoRegistroType estado,
			int numeroLinea, String clienteCodigo, String servicioCodigoAlterno, String numeroSolicitud, String prefijo,
			String numeroSolicitudSinPrefijo, LocalDate femi, LocalDate fema, String nota, String productoCodigoAlterno,
			String productoNombre, int cantidad, String unidadMedidaCodigoAlterno, String bodegaCodigoAlterno,
			String estadoInventarioCodigoAlterno, String lote, Long idCliente, Long idServicio, Long idProducto,
			Long idUnidadMedida, Long idBodega, String idEstadoInventario, boolean requiereBom) {
		super(id, version, FechaCreacion, createdBy, FechaModificacion, modifiedBy, idArchivo, estado, numeroLinea,
				clienteCodigo, servicioCodigoAlterno, numeroSolicitud, prefijo, numeroSolicitudSinPrefijo, femi, fema,
				nota, productoCodigoAlterno, productoNombre, cantidad, unidadMedidaCodigoAlterno, bodegaCodigoAlterno,
				estadoInventarioCodigoAlterno, lote, idCliente, idServicio, idProducto, idUnidadMedida, idBodega,
				idEstadoInventario);
		this.requiereBom = requiereBom;
	}
}