package com.egakat.io.gws.cliente.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.egakat.core.dto.EntityDto;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DocumentoDespachoClienteDto extends EntityDto<Integer> {

	@JsonProperty("idSolicitud")
	public Long idSolicitud;

	@JsonProperty("prefijoPedido")
	public String prefijoPedido;

	@JsonProperty("numeroPedido")
	public String numeroPedido;

	@JsonProperty("prefijoFactura")
	public String prefijoFactura;

	@JsonProperty("numeroFactura")
	public String numeroFactura;

	@JsonProperty("documentoLineas")
	@Valid
	public List<DocumentoDespachoClienteLineaDto> lineas = null;

	@JsonIgnore
	@Valid
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