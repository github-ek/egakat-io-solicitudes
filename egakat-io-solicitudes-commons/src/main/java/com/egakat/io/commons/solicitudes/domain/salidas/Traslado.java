package com.egakat.io.commons.solicitudes.domain.salidas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.io.commons.solicitudes.domain.Solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "traslados")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Traslado extends Solicitud {

	public static final String VALOR_UNITARIO_DECLARADO = "VALOR_UNITARIO_DECLARADO";

	public static final String BODEGA_TRASLADO_CODIGO_ALTERNO = "BODEGA_TRASLADO_CODIGO_ALTERNO";
	public static final String ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO = "ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO";

	@Column(name = "valor_unitario_declarado")
	private Long valorUnitarioDeclarado;
	

	@Column(name = "bodega_traslado_codigo_alterno", length = 50)
	@NotEmpty
	private String bodegaTrasladoCodigoAlterno;

	@Column(name = "estado_inventario_traslado_codigo_alterno", length = 50)
	@NotNull
	private String estadoInventarioTrasladoCodigoAlterno;

	
	@Column(name = "id_bodega_traslado")
	private Long idBodegaTraslado;

	@Column(name = "id_estado_inventario_traslado", length = 4)
	private String idEstadoInventarioTraslado;

	
	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case BODEGA_TRASLADO_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO:
			return getStringValueFromHomologableProperty(property);
			
		case VALOR_UNITARIO_DECLARADO:
			return getValorUnitarioDeclarado();
		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Override
	public boolean isHomologableProperty(String property) {
		switch (property) {
		case BODEGA_TRASLADO_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO:
			return true;
		default:
			return super.isHomologableProperty(property);
		}
	}

	@Override
	protected String getStringValueFromHomologableProperty(String property) {
		switch (property) {
		case BODEGA_TRASLADO_CODIGO_ALTERNO:
			return getBodegaTrasladoCodigoAlterno();
		case ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO:
			return getEstadoInventarioTrasladoCodigoAlterno();
		default:
			return super.getStringValueFromHomologableProperty(property);
		}
	}

	@Override
	protected Object getObjectValueFromHomologousProperty(String property) {
		switch (property) {
		case BODEGA_TRASLADO_CODIGO_ALTERNO:
			return getIdBodegaTraslado();
		case ESTADO_INVENTARIO_TRASLADO_CODIGO_ALTERNO:
			return getIdEstadoInventarioTraslado();
		default:
			return super.getObjectValueFromHomologousProperty(property);
		}
	}
}