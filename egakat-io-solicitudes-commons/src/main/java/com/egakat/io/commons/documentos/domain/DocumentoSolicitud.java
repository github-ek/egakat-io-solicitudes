package com.egakat.io.commons.documentos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.egakat.integration.domain.IntegracionEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "documentos_solicitudes")
@AttributeOverride(name = "id", column = @Column(name = "id_documento_solicitud"))
@DynamicUpdate
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DocumentoSolicitud extends IntegracionEntity {

	@Column(name = "cliente_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String clienteCodigoAlterno;
	
	@Column(name = "tipo_documento_codigo_alterno", length = 50, nullable = false)
	@NotNull
	@Size(max = 50)
	private String tipoDocumentoCodigoAlterno;
	
	@Column(name = "numero_solicitud", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroSolicitud;
	
	@Column(name = "prefijo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String prefijo;
	
	@Column(name = "numero_solicitud_sin_prefijo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroSolicitudSinPrefijo;
	
	@Column(name = "numero_documento", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroDocumento;
	
	@Column(name = "prefijo_documento", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String prefijoDocumento;
	
	@Column(name = "numero_documento_sin_prefijo", length = 20, nullable = false)
	@NotNull
	@Size(max = 20)
	private String numeroDocumentoSinPrefijo;

	@Column(name = "id_cliente")
	private Long idCliente;

	@Column(name = "id_tipo_documento")
	private Long idTipoDocumento;

	@Column(name = "id_solicitud")
	private Long idSolicitud;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitud", cascade = CascadeType.ALL)
	private List<DocumentoSolicitudLinea> lineas = new ArrayList<>();

	public void add(DocumentoSolicitudLinea item) {
		lineas.add(item);
		item.setSolicitud(this);
	}

	public void remove(DocumentoSolicitudLinea item) {
		lineas.remove(item);
		item.setSolicitud(null);
	}
}