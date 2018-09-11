package com.egakat.io.gws.commons.ordenes.repository;

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