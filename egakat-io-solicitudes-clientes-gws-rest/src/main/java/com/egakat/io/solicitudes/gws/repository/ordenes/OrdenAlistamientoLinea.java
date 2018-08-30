package com.egakat.io.solicitudes.gws.repository.ordenes;

public interface OrdenAlistamientoLinea {

	String getOrdlin();
	
	int getNumeroLinea();

	String getNumeroLineaExterno();

	String getNumeroSublineaExterno();

	String getProductoCodigoAlterno();

	String getProductoNombre();

	String getBodegaCodigoAlterno();

	String getEstadoInventarioCodigoAlterno();

	int getCantidad();
}