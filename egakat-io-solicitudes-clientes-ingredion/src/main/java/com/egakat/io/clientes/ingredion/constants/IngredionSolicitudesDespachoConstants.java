package com.egakat.io.clientes.ingredion.constants;

import java.time.LocalTime;

public class IngredionSolicitudesDespachoConstants {

	public static final String INTEGRACION_CODIGO = "INGREDION_SOLICITUDES_DESPACHO";

	public static final String CLIENTE_CODIGO = "INGREDION";

	public static final String SERVICIO_DESPACHO_CODIGO = "SALIDA";

	public static final String CANAL_CODIGO = "ENTREGA DIRECTA";

	public static final LocalTime HORA_MINIMA = LocalTime.of(8, 0, 0);

	public static final LocalTime HORA_MAXIMA = LocalTime.of(17, 0, 0);

	public static final String ESTADO_INVENTARIO = "A";

	public static final String TIPO_DOC_IDENTIFICADOR = "IDENTIFICADOR";

}
