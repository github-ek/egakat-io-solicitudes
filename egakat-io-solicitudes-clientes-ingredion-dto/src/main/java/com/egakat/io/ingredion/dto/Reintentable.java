package com.egakat.io.ingredion.dto;

import java.time.LocalDateTime;

public interface Reintentable<ID> {
	
	ID getId();

	String getIdExterno();

	void setIdExterno(String value);

	String getEstadoIntegracion();

	void setEstadoIntegracion(String value);

	String getSubEstadoIntegracion();

	void setSubEstadoIntegracion(String value);

	int getReintentos();

	void setReintentos(int value);

	LocalDateTime getFechaCreacion();

	void setFechaCreacion(LocalDateTime value);

	LocalDateTime getFechaModificacion();

	void setFechaModificacion(LocalDateTime value);
}
