package com.egakat.io.commons.solicitudes.domain.manufacturas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.io.commons.solicitudes.domain.Solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "manufacturas")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Manufactura extends Solicitud {

	public static final String REQUIERE_BOM = "REQUIERE_BOM";

	@Column(name = "requiere_bom")
	private boolean requiereBom;

	@Override
	public Object getObjectValueFromProperty(String property) {
		switch (property) {
		case REQUIERE_BOM:
			return isRequiereBom();

		default:
			return super.getObjectValueFromProperty(property);
		}
	}
}