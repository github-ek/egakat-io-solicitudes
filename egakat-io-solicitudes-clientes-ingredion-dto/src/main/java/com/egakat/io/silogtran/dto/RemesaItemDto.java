package com.egakat.io.silogtran.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id_producto_econnect",
"codigo_producto",
"producto_nombre",
"producto_codigoministerio",
"naturaleza_carga",
"tipo_producto",
"codigo_empaque",
"peso",
"peso_bruto",
"cantidad",
"volumen",
"valor_declarado",
"descripcion_detalle_remesa",
"predistribucion",
"factor_conversion",
"cantidad_embalaje",
"lote",
"estado_inventario_nombre",
"fecha_vencimiento"
})
public class RemesaItemDto {

@JsonProperty("id_producto_econnect")
public String idProductoEconnect;
@JsonProperty("codigo_producto")
public String codigoProducto;
@JsonProperty("producto_nombre")
public String productoNombre;
@JsonProperty("producto_codigoministerio")
public String productoCodigoministerio;
@JsonProperty("naturaleza_carga")
public String naturalezaCarga;
@JsonProperty("tipo_producto")
public String tipoProducto;
@JsonProperty("codigo_empaque")
public String codigoEmpaque;
@JsonProperty("peso")
public String peso;
@JsonProperty("peso_bruto")
public String pesoBruto;
@JsonProperty("cantidad")
public String cantidad;
@JsonProperty("volumen")
public String volumen;
@JsonProperty("valor_declarado")
public String valorDeclarado;
@JsonProperty("descripcion_detalle_remesa")
public String descripcionDetalleRemesa;
@JsonProperty("predistribucion")
public String predistribucion;
@JsonProperty("factor_conversion")
public String factorConversion;
@JsonProperty("cantidad_embalaje")
public String cantidadEmbalaje;
@JsonProperty("lote")
public String lote;
@JsonProperty("estado_inventario_nombre")
public String estadoInventarioNombre;
@JsonProperty("fecha_vencimiento")
public String fechaVencimiento;
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