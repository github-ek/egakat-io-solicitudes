package com.egakat.io.solicitudes.gws.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.commons.domain.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "errores_integracion")
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_error_integracion"))
public class ErrorIntegracion extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Column(name = "integracion", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String integracion;

	@Column(name = "id_externo", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String idExterno;

	@Column(name = "codigo", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String codigo;

	@Column(name = "mensaje", length = -1, nullable = false)
	@NotNull
	private String mensaje;

	@Column(name = "arg0", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg0;

	@Column(name = "arg1", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg1;

	@Column(name = "arg2", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg2;

	@Column(name = "arg3", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg3;

	@Column(name = "arg4", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg4;

	@Column(name = "arg5", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg5;

	@Column(name = "arg6", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg6;

	@Column(name = "arg7", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg7;

	@Column(name = "arg8", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg8;

	@Column(name = "arg9", length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String arg9;
}