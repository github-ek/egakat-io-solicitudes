package com.egakat.io.clientes.gws.constants;

public class RestConstants {

	final public static String BASE = "/api/v1/integraciones";
	
	final public static String SOLICITUDES_DESPACHO = BASE + "/solicitudes/salidas";
	
	final public static String SOLICITUDES_DESPACHO_BY_STATUS = "?status={status}";

	final public static String ORDENES_ALISTAMIENTO = BASE + "/solicitudes/ordenalistamiento";

	final public static String DOCUMENTOS_SOLICITUDES_DESPACHO = BASE + "/solicitudes/documentoDespacho";
	
	final public static String DOCUMENTOS_SOLICITUDES_DESPACHO_BY_STATUS = "?status={status}";
}
