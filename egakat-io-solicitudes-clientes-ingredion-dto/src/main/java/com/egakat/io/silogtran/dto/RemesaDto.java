
package com.egakat.io.silogtran.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.egakat.io.ingredion.dto.Reintentable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemesaDto implements Reintentable<Long> {
	@JsonIgnore
	private Long id;
	@JsonIgnore
	private String idExterno;
	@JsonIgnore
	private String estadoIntegracion;
	@JsonIgnore
	private String subEstadoIntegracion;
	@JsonIgnore
	private int reintentos;
	@JsonIgnore
	private LocalDateTime fechaCreacion;
	@JsonIgnore
	private LocalDateTime fechaModificacion;

	@JsonIgnore
	private String numeroConfirmacionSilogtran;
	@JsonIgnore
	private LocalDateTime fechaIntegracionSilogtran;

	
	@JsonProperty("centro_costo")
	public String centroCosto;
	@JsonProperty("tipo_remesa")
	public String tipoRemesa;
	@JsonProperty("codigo_cliente")
	public String codigoCliente;
	@JsonProperty("division_cliente")
	public String divisionCliente;
	@JsonProperty("nombre_remitente")
	public String nombreRemitente;
	@JsonProperty("tipo_documento_remitente")
	public String tipoDocumentoRemitente;
	@JsonProperty("documento_remitente")
	public String documentoRemitente;
	@JsonProperty("direccion_remitente")
	public String direccionRemitente;
	@JsonProperty("telefono_remitente")
	public String telefonoRemitente;
	@JsonProperty("contacto_remitente")
	public String contactoRemitente;
	@JsonProperty("ciudad_remitente")
	public String ciudadRemitente;
	@JsonProperty("departamento_remitente")
	public String departamentoRemitente;
	@JsonProperty("nombre_destinatario")
	public String nombreDestinatario;
	@JsonProperty("tipo_documento_destinatario")
	public String tipoDocumentoDestinatario;
	@JsonProperty("documento_destinatario")
	public String documentoDestinatario;
	@JsonProperty("direccion_destinatario")
	public String direccionDestinatario;
	@JsonProperty("telefono_destinatario")
	public String telefonoDestinatario;
	@JsonProperty("contacto_destinatario1")
	public String contactoDestinatario1;
	@JsonProperty("contacto_destinatario2")
	public String contactoDestinatario2;
	@JsonProperty("ciudad_destinatario")
	public String ciudadDestinatario;
	@JsonProperty("departamento_destinatario")
	public String departamentoDestinatario;
	@JsonProperty("zona_ciudad_destinatario")
	public String zonaCiudadDestinatario;
	@JsonProperty("coordenada_x_longitud")
	public String coordenadaXLongitud;
	@JsonProperty("coordenada_y_latitud")
	public String coordenadaYLatitud;
	@JsonProperty("observacion_remesa")
	public String observacionRemesa;
	@JsonProperty("fecha_compromiso_minima")
	public String fechaCompromisoMinima;
	@JsonProperty("fecha_compromiso_maxima")
	public String fechaCompromisoMaxima;
	@JsonProperty("hora_compromiso_maxima")
	public String horaCompromisoMaxima;
	@JsonProperty("hora_compromiso_minima")
	public String horaCompromisoMinima;
	@JsonProperty("placa_vehiculo")
	public String placaVehiculo;
	@JsonProperty("secuencia_entrega")
	public Integer secuenciaEntrega;
	@JsonProperty("tipo_remision")
	public String tipoRemision;
	@JsonProperty("remision")
	public String remision;
	@JsonProperty("documento_wms")
	public String documentoWms;
	@JsonProperty("documento_numero_solicitud")
	public String documentoNumeroSolicitud;
	@JsonProperty("id_ordentransporte")
	public Long idOrdentransporte;
	@JsonProperty("punto_codigo_alterno")
	public String puntoCodigoAlterno;
	@JsonProperty("regional")
	public String regional;
	@JsonProperty("ciudad_nombre_alterno")
	public String ciudadNombreAlterno;
	@JsonProperty("bodega_codigo_alterno")
	public String bodegaCodigoAlterno;
	@JsonProperty("programa")
	public String programa;
	@JsonProperty("planta")
	public String planta;
	@JsonProperty("periodo_acta")
	public String periodoActa;
	@JsonProperty("items")
	public List<RemesaItemDto> items = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}