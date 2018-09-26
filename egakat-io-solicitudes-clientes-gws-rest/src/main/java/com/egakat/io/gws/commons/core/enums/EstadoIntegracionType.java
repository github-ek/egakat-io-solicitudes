package com.egakat.io.gws.commons.core.enums;

public enum EstadoIntegracionType {
	// @formatter:off
    NO_PROCESADO,
    ERROR_ESTRUCTURA,
    CORREGIDO,
    ESTRUCTURA_VALIDA, 
    
    ERROR_ENRIQUECIMIENTO,
    ERROR_HOMOLOGACION,
    HOMOLOGADO,
    
    ERROR_VALIDACION, 
    VALIDADO, 
    DESCARTADO,
    
    ERROR_CARGUE,
    PROCESADO,

    DETENIDO;
	// @formatter:on

	public boolean isError() {
		switch (this) {
		case ERROR_ESTRUCTURA:
		case ERROR_ENRIQUECIMIENTO:
		case ERROR_HOMOLOGACION:
		case ERROR_VALIDACION:
		case ERROR_CARGUE:
			return true;
		default:
			return false;
		}
	}
}
