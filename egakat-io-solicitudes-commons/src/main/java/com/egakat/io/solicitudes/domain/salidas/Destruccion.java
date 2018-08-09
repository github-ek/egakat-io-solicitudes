package com.egakat.io.solicitudes.domain.salidas;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.io.solicitudes.domain.Solicitud;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "destrucciones")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Destruccion extends Solicitud {

	@Builder
	public Destruccion(Long id, int version, LocalDateTime fechaCreacion, String creadoPor,
			LocalDateTime fechaModificacion, String modificadoPor, Long idArchivo, @NotNull EstadoRegistroType estado,
			int numeroLinea, @NotEmpty String clienteCodigo, @NotEmpty String servicioCodigoAlterno,
			@NotEmpty String numeroSolicitud, @NotNull String prefijo, @NotNull String numeroSolicitudSinPrefijo,
			LocalDate femi, LocalDate fema, @NotNull String nota, @NotNull String productoCodigoAlterno,
			@NotNull String productoNombre, int cantidad, @NotNull String unidadMedidaCodigoAlterno,
			@NotEmpty String bodegaCodigoAlterno, @NotNull String estadoInventarioCodigoAlterno, @NotNull String lote,
			Long idCliente, Long idServicio, Long idProducto, Long idUnidadMedida, Long idBodega,
			String idEstadoInventario) {
		super(id, version, fechaCreacion, creadoPor, fechaModificacion, modificadoPor, idArchivo, estado, numeroLinea,
				clienteCodigo, servicioCodigoAlterno, numeroSolicitud, prefijo, numeroSolicitudSinPrefijo, femi, fema,
				nota, productoCodigoAlterno, productoNombre, cantidad, unidadMedidaCodigoAlterno, bodegaCodigoAlterno,
				estadoInventarioCodigoAlterno, lote, idCliente, idServicio, idProducto, idUnidadMedida, idBodega,
				idEstadoInventario);
	}
}