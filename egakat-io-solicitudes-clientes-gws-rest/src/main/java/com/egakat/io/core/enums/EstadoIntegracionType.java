package com.egakat.io.core.enums;

public enum EstadoIntegracionType {
	// @formatter:off
    NO_PROCESADO,
    CORREGIDO,
    
    ERROR_ESTRUCTURA,
    ESTRUCTURA_VALIDA, 
    
    ERROR_HOMOLOGACION,
    HOMOLOGADO,
    
    ERROR_VALIDACION, 
    VALIDADO, 
    
    ERROR_CARGUE,
    CARGADO,
    
    EN_PROCESO,
    ERROR_PROCESAMIENTO,
    PROCESADO
    ;
	// @formatter:on

	public boolean isError() {
		switch (this) {
		case ERROR_ESTRUCTURA:
		case ERROR_HOMOLOGACION:
		case ERROR_VALIDACION:
		case ERROR_CARGUE:
		case ERROR_PROCESAMIENTO:
			return true;
		default:
			return false;
		}
	}
}
