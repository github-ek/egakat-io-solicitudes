package com.egakat.core.io.stage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.core.io.stage.enums.EstadoIntegracionType;
import com.egakat.core.io.stage.enums.EstadoNotificacionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "integraciones_actualizaciones")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ActualizacionIntegracion extends IntegrationEntity {
	
	@Column(name = "estado_integracion", length = 50, nullable = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private EstadoIntegracionType estadoIntegracion;

	@Column(name = "subestado_integracion", length = 50, nullable = false)
	@Size(max = 50)
	@NotNull
	private String subEstadoIntegracion;

	@Column(name = "estado_notificacion", length = 50, nullable = true)
	@Enumerated(EnumType.STRING)
	private EstadoNotificacionType estadoNotificacion;

	@Column(name = "entradas_en_cola", nullable = false)
	private int entradasEnCola;
	
	@Column(name = "arg0", length = 100, nullable = true)
	@Size(max = 100)
	private String arg0;

	@Column(name = "arg1", length = 100, nullable = true)
	@Size(max = 100)
	private String arg1;

	@Column(name = "arg2", length = 100, nullable = true)
	@Size(max = 100)
	private String arg2;

	@Column(name = "arg3", length = 100, nullable = true)
	@Size(max = 100)
	private String arg3;

	@Column(name = "arg4", length = 100, nullable = true)
	@Size(max = 100)
	private String arg4;

	@Column(name = "arg5", length = 100, nullable = true)
	@Size(max = 100)
	private String arg5;

	@Column(name = "arg6", length = 100, nullable = true)
	@Size(max = 100)
	private String arg6;

	@Column(name = "arg7", length = 100, nullable = true)
	@Size(max = 100)
	private String arg7;

	@Column(name = "arg8", length = 100, nullable = true)
	@Size(max = 100)
	private String arg8;

	@Column(name = "arg9", length = 100, nullable = true)
	@Size(max = 100)
	private String arg9;

	@Column(name = "datos", length = -1, nullable = true)
	private String datos;
}