package com.egakat.io.commons.solicitudes.domain.manufacturas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.io.commons.solicitudes.domain.Solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@Entity
@Table(name = "manufacturas_bom")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ManufacturaBom extends Solicitud {
	
	public static final String REQUIERE_BOM = "REQUIERE_BOM";
	public static final String PRODUCTO_BOM_CODIGO_ALTERNO = "PRODUCTO_BOM_CODIGO_ALTERNO";
	public static final String CANTIDAD_BOM = "CANTIDAD_BOM";
	public static final String ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO = "ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO";
	public static final String SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO = "SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO";
	
	@Column(name = "requiere_bom")
	private boolean requiereBom;

	@Column(name = "producto_bom_codigo_alterno", length = 50)
	@NotNull
	private String productoBomCodigoAlterno;

	@Column(name = "cantidad_bom")
	private int cantidadBom;

	@Column(name = "estado_inventario_bom_codigo_alterno", length = 50)
	@NotNull
	private String estadoInventarioBomCodigoAlterno;

	@Column(name = "subestado_inventario_bom_codigo_alterno", length = 50)
	@NotNull
	private String subestadoInventarioBomCodigoAlterno;

	@Column(name = "id_producto_bom")
	private Long idProductoBom;

	@Column(name = "id_estado_inventario_bom", length = 4)
	private String idEstadoInventarioBom;

	@Column(name = "id_subestado_inventario_bom", length = 4)
	private String idSubestadoInventarioBom;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case PRODUCTO_BOM_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
		case SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return getStringValueFromHomologableProperty(property);

		case CANTIDAD_BOM:
			return getCantidadBom();
		case REQUIERE_BOM:
			return isRequiereBom();
			
		default:
			return super.getObjectValueFromProperty(property);
		}
	}

	@Override
	public boolean isHomologableProperty(String property) {
		switch (property) {
		case PRODUCTO_BOM_CODIGO_ALTERNO:
		case ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
		case SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return true;
		default:
			return super.isHomologableProperty(property);
		}
	}

	@Override
	protected String getStringValueFromHomologableProperty(String property) {
		switch (property) {
		case PRODUCTO_BOM_CODIGO_ALTERNO:
			return getProductoBomCodigoAlterno();
		case ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return getEstadoInventarioBomCodigoAlterno();
		case SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return getSubestadoInventarioBomCodigoAlterno();

		default:
			return super.getStringValueFromHomologableProperty(property);
		}
	}

	@Override
	protected Object getObjectValueFromHomologousProperty(String property) {
		switch (property) {
		case PRODUCTO_BOM_CODIGO_ALTERNO:
			return getIdProductoBom();
		case ESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return getIdEstadoInventarioBom();
		case SUBESTADO_INVENTARIO_BOM_CODIGO_ALTERNO:
			return getIdSubestadoInventarioBom();

		default:
			return super.getObjectValueFromHomologousProperty(property);
		}
	}

	public String getIdGrupo() {
		val sb = new StringBuilder();
		sb.append(this.getIdBodega()).append("|");
		sb.append(this.getIdCliente()).append("|");
		sb.append(this.getNumeroSolicitud()).append("|");
		sb.append(this.getIdProducto()).append("|");
		sb.append(this.getIdEstadoInventario()).append("|");
		return sb.toString();
	}
}