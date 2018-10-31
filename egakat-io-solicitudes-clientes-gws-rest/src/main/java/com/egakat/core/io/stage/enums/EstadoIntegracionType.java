package com.egakat.core.io.stage.enums;

public enum EstadoIntegracionType {
	// @formatter:off
    NO_PROCESADO,
    REINTENTAR,
    CORREGIDO,
    
    ERROR_ESTRUCTURA,
    ESTRUCTURA_VALIDA, 
    
    ERROR_HOMOLOGACION,
    HOMOLOGADO,
    
    ERROR_VALIDACION, 
    VALIDADO, 
    DESCARTADO,
    
    ERROR_CARGUE,
    CARGADO,
    
    PROCESADO,
    DETENIDO,
    FINALIZADO
    ;
	// @formatter:on

	public boolean isReintento() {
		switch (this) {
		case REINTENTAR:
			return true;
		default:
			return false;
		}
	}

	public boolean isError() {
		switch (this) {
		case ERROR_ESTRUCTURA:
		case ERROR_HOMOLOGACION:
		case ERROR_VALIDACION:
		case ERROR_CARGUE:
			return true;
		default:
			return false;
		}
	}
}
