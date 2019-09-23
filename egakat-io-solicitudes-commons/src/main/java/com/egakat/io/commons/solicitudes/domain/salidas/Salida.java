package com.egakat.io.commons.solicitudes.domain.salidas;

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
@Table(name = "salidas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Salida extends SolicitudTercero {

	public static final String PREDISTRIBUCION_CROSSDOCK = "PREDISTRIBUCION_CROSSDOCK";

	public static final String AUTORIZADO_IDENTIFICACION = "AUTORIZADO_IDENTIFICACION";
	public static final String AUTORIZADO_NOMBRES = "AUTORIZADO_NOMBRES";

	public static final String REQUIERE_RECAUDO = "REQUIERE_RECAUDO";
	public static final String VALOR_RECAUDAR = "VALOR_RECAUDAR";

	@Column(name = "predistribucion_crossdock", length = 200)
	@NotNull
	private String predistribucionCrossdock;

	@Column(name = "autorizado_identificacion", length = 20)
	@NotNull
	private String autorizadoIdentificacion;

	@Column(name = "autorizado_nombres", length = 100)
	@NotNull
	private String autorizadoNombres;

	@Column(name = "requiere_recaudo", length = 1)
	@NotNull
	private String requiereRecaudo;

	@Column(name = "valor_recaudar")
	private Integer valorRecaudar;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case PREDISTRIBUCION_CROSSDOCK:
			return getPredistribucionCrossdock();

		case AUTORIZADO_IDENTIFICACION:
			return getAutorizadoIdentificacion();
		case AUTORIZADO_NOMBRES:
			return getAutorizadoNombres();

		case REQUIERE_RECAUDO:
			return getRequiereRecaudo();
		case VALOR_RECAUDAR:
			return getValorRecaudar();
		default:
			return super.getObjectValueFromProperty(property);
		}
	}
}