package com.egakat.io.solicitudes.gws.domain.dqs;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
abstract public class HomologableEntry extends DataQualityEntry {

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	final public boolean propertyIsNullOrEmpty(String property) {
		boolean result;
		val value = getObjectValueFromProperty(property);

		if (value == null) {
			result = true;
		} else {
			if (value instanceof String) {
				result = "".equals(((String) value).trim());
			} else {
				result = false;
			}
		}

		return result;
	}

	abstract public Object getObjectValueFromProperty(String property);

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	final public String getHomologablePropertyValue(String property) {
		if (!isHomologableProperty(property)) {
			throw new IllegalArgumentException("El campo \"" + property + "\", no es homologable.");
		}

		String result = getStringValueFromHomologableProperty(property);
		if (result == null) {
			result = "";
		}
		return result;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	final public boolean propertyHasBeenHomologated(String property) {
		boolean result = false;
		if (isHomologableProperty(property)) {
			val value = getObjectValueFromHomologousProperty(property);
			result = (value != null);
		}
		return result;
	}

	abstract public boolean isHomologableProperty(String property);

	abstract protected String getStringValueFromHomologableProperty(String property);

	abstract protected Object getObjectValueFromHomologousProperty(String property);

	public HomologableEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HomologableEntry(Long id, int version, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion,
			@NotNull @Size(max = 50) String integracion, @NotNull @Size(max = 100) String idExterno,
			@NotNull @Size(max = 100) String correlacion,
			@NotNull @Size(max = 50) EstadoIntegracionType estadoIntegracion) {
		super(id, version, fechaCreacion, fechaModificacion, integracion, idExterno, correlacion, estadoIntegracion);
		// TODO Auto-generated constructor stub
	}
}
