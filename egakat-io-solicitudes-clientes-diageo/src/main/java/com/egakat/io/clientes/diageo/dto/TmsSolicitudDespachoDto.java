package com.egakat.io.clientes.diageo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class TmsSolicitudDespachoDto {
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String TIME_FORMAT = "HH:mm";

	@ApiModelProperty(notes = "Codigo del cliente. Para DIAGEO usar DIAGEO. Se deja abierto para que se pueda usar este servicio con cualquier otro cliente. Usar SELECT codigo FROM eConnect.dbo.clientes.", position = 1)
	@NotNull
	@Size(max = 20)
	private String clienteCodigoAlterno;

	@ApiModelProperty(notes = "SALIDA. Podemos omitir el envio de este atributo, pero se deja abierto", position = 2)
	@NotNull
	@Size(max = 50)
	private String servicioCodigoAlterno;

	@ApiModelProperty(notes = "Número de pedido del cliente. Debe ser unico por cliente", position = 3)
	@NotNull
	@Size(max = 20)
	private String numeroSolicitud;

	@ApiModelProperty(notes = "Desde cuando se va a hacer la entrega", position = 4)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate fechaMinima;

	@ApiModelProperty(notes = "Hasta cuando se va a hacer la entrega", position = 5)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@NotNull
	private LocalDate fechaMaxima;

	@ApiModelProperty(notes = "Desde que hora se va a hacer la entrega", position = 6)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
	private LocalTime horaMaxima;

	@ApiModelProperty(notes = "Hasta que hora se va a hacer la entrega", position = 7)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
	private LocalTime horaMinima;

	@ApiModelProperty(notes = "NIT o identificación del cliente final ", position = 8)
	@NotNull
	@Size(max = 20)
	private String terceroIdentificacion;

	@ApiModelProperty(notes = "Nombre del cliente final ", position = 9)
	@NotNull
	@Size(max = 100)
	private String terceroNombre;

	@ApiModelProperty(notes = "Canal o segmento de entrega, e.g. CADENAS, DISTRIBUIDORES, OFF TRADE, ON TRADE.... Transporte usa este dato para separar las solicitudes dependiendo del tipo de cliente final. No es lo mismo planificar los despachos para las cadenas de almacen que para la Zona T. Ver SELECT codigo FROM eConnect.dbo.canales", position = 10)
	@NotNull
	@Size(max = 50)
	private String canalCodigoAlterno;

	@ApiModelProperty(notes = "Codigo DANE. Usamos el codigo DANE de 2018, sin embargo lo mejor es que vean los que estan en: SELECT b.codigo, b.nombre, a.codigo, a.nombre_alterno FROM eConnect.dbo.ciudades a INNER JOIN eConnect.dbo.departamentos b ON b.id_departamento = a.id_departamento WHERE b.id_pais = 1 ORDER BY a.codigo", position = 11)
	@NotNull
	@Size(max = 50)
	private String ciudadCodigoAlterno;

	@ApiModelProperty(notes = "Dirección de entrega. Detalles como indicaciones de como llegar podrian hacer que el georeferenciador automatico falle y obligue a tareas manuales", position = 12)
	@NotNull
	@Size(max = 150)
	private String direccion;

	@ApiModelProperty(notes = "Algunas cadenas de almacenes tienen sus puntos de entrega codificados. Aqui iria el codigo", position = 13)
	@NotNull
	@Size(max = 50)
	private String puntoCodigoAlterno;

	@ApiModelProperty(notes = "Algunas cadenas de almacenes tienen sus puntos de entrega codificados. Aqui iria el nombreo descripción del punto", position = 14)
	@NotNull
	@Size(max = 50)
	private String puntoNombre;

	@ApiModelProperty(notes = "Nombre de la persona contacto en el cliente final a quien debe contactarse en caso de alguna novedad", position = 15)
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalNombre;

	@ApiModelProperty(notes = "Telefono de la persona contacto en el cliente final a quien debe contactarse en caso de alguna novedad", position = 16)
	@NotNull
	@Size(max = 100)
	private String contactoPrincipalTelefono;

	@ApiModelProperty(notes = "En el caso de las cadenas, a veces se requiere conocer el numero de la orden de compra", position = 17)
	@NotNull
	@Size(max = 20)
	private String numeroOrdenCompra;

	@ApiModelProperty(notes = "En el caso de las cadenas, a veces se requiere conocer la fecha de la orden de compra, porque de acuerdo con este fecha se pueden complementar los limites para prestar el servicio", position = 18)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	private LocalDate fechaOrdenCompra;

	@ApiModelProperty(notes = "Observaciones de la entrega", position = 19)
	@NotNull
	@Size(max = 200)
	private String nota;

	@ApiModelProperty(notes = "Numero de la orden como aparece en WMS. Ver ttcwmsprd.ord.ordnum", position = 19)
	@NotNull
	@Size(max = 35)
	private String ordnum;

	
	@ApiModelProperty(notes = "Líneas", position = 20)
	private List<LineaDto> lineas;

	@Getter
	@Setter
	@ToString(callSuper = true)
	@NoArgsConstructor
	public static class LineaDto {

		@ApiModelProperty(notes = "Consecutivo de linea", position = 1)
		@NotNull
		@Size(max = 50)
		private String numeroLineaExterno;

		@ApiModelProperty(notes = "Código del producto como aparece en WMS", position = 2)
		@NotEmpty
		@Size(max = 50)
		private String productoCodigo;

		@ApiModelProperty(notes = "Nombre del producto como aparece en WMS", position = 3)
		@NotNull
		@Size(max = 200)
		private String productoNombre;

		@ApiModelProperty(notes = "Numero de unidades. No cajas.", position = 4)
		private int cantidad;

		@ApiModelProperty(notes = "Codigo de la bodega WMS. Debe ser el mismo en todas las lineas. Ver SELECT codigo,nombre FROM eConnect.dbo.bodegas ", position = 5)
		@NotEmpty
		@Size(max = 50)
		private String bodegaCodigoAlterno;

		@ApiModelProperty(notes = "Estado de inventario de la mercancia de como se va a sacar de bodega WMS. Ver SELECT id_estado_inventario FROM eConnect.dbo.estados_inventario ", position = 6)
		@NotEmpty
		@Size(max = 50)
		private String estadoCodigoAlterno;

	}
}
