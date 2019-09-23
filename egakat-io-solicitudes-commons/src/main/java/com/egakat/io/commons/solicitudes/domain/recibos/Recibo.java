package com.egakat.io.commons.solicitudes.domain.recibos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.io.commons.solicitudes.domain.SolicitudTercero;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "recibos")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Recibo extends SolicitudTercero {

	public static final String BL = "BL";
	public static final String CONTENEDOR = "CONTENEDOR";

	@Column(length = 50, nullable = false)
	@NotNull
	private String bl;

	@Column(length = 50, nullable = false)
	@NotNull
	private String contenedor;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case BL:
			return getBl();
		case CONTENEDOR:
			return getContenedor();
		default:
			return super.getObjectValueFromProperty(property);
		}
	}
}