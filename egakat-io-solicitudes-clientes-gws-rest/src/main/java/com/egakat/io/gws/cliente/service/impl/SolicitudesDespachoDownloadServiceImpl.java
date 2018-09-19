package com.egakat.io.gws.cliente.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.configuration.RestProperties;
import com.egakat.io.gws.cliente.dto.SolicitudClienteDto;
import com.egakat.io.gws.cliente.dto.SolicitudClienteLineaDto;
import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.service.api.DownloadService;
import com.egakat.io.gws.commons.core.service.impl.DownloadServiceImpl;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.gws.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.configuration.properties.SolicitudDespachoClienteRestProperties;

import lombok.val;

@Service
public class SolicitudesDespachoDownloadServiceImpl
		extends DownloadServiceImpl<SolicitudClienteDto, SolicitudDespachoDto, String> implements DownloadService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private SolicitudDespachoClienteRestProperties properties;

	@Autowired
	private RestClient restClient;

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_SALIDAS;
	}

	@Override
	protected String getApiEndPoint() {
		return IntegracionesRestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "/{id}";
	}

	protected Class<SolicitudClienteDto> getResponseType() {
		return SolicitudClienteDto.class;
	}

	@Override
	protected SolicitudClienteDto getInput(ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, getResponseType(), actualizacion.getIdExterno());

		val result = response.getBody();
		return result;
	}

	@Override
	protected void validate(ActualizacionIntegracionDto actualizacion, SolicitudClienteDto input,
			List<ErrorIntegracionDto> errores) {
		super.validate(actualizacion, input, errores);

		if (input.getFemi() == null) {
			errores.add(errorAtributoRequeridoNoSuministrado(actualizacion, "femi"));
		}

		if (input.getFema() == null) {
			errores.add(errorAtributoRequeridoNoSuministrado(actualizacion, "fema"));
		}

		if (input.getHomi() == null) {
			errores.add(errorAtributoRequeridoNoSuministrado(actualizacion, "homi"));
		}

		if (input.getHoma() == null) {
			errores.add(errorAtributoRequeridoNoSuministrado(actualizacion, "homa"));
		}
	}

	@Override
	protected SolicitudDespachoDto asModel(ActualizacionIntegracionDto actualizacion, SolicitudClienteDto input) {
		val id = String.valueOf(input.getId());

		val prefijo = defaultString(input.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(input.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;

		val fechaCreacionExterna = LocalDateTime.now();

		
		val lineas = asLineas(input);

		// @formatter:off
		val result = SolicitudDespachoDto
					.builder() 
					.integracion(actualizacion.getIntegracion())
					.idExterno(id)
					.correlacion(actualizacion.getCorrelacion())
					.clienteCodigoAlterno(defaultString(input.getClienteCodigoAlterno()))
					.servicioCodigoAlterno(defaultString(input.getServicioCodigoAlterno()))
					.numeroSolicitud(numeroSolicitud)
					.prefijo(prefijo)
					.numeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo)
					.femi(input.getFemi())
					.fema(input.getFema())
					.homi(input.getHomi())
					.homa(input.getHoma())
					.requiereTransporte(requiereTransporte)
					.requiereAgendamiento(requiereAgendamiento)
					.requiereDespacharCompleto(requiereDespacharCompleto)
					.terceroIdentificacion(defaultString(input.getTerceroIdentificacion()))
					.terceroNombre(defaultString(input.getTerceroNombre()))
					.canalCodigoAlterno(defaultString(input.getCanalCodigoAlterno()))
					.ciudadCodigoAlterno(defaultString(input.getCiudadCodigoAlterno()))
					.direccion(defaultString(input.getDireccion()))
					.puntoCodigoAlterno(defaultString(input.getPuntoCodigoAlterno()))
					.puntoNombre("")
					.autorizadoIdentificacion("")
					.autorizadoNombres("")
					.numeroOrdenCompra(defaultString(input.getNumeroOrdenCompra()))
					.nota(defaultString(input.getNota()))
					.fechaCreacionExterna(fechaCreacionExterna)
					.lineas(lineas)
					.build();
			// @formatter:on

		return result;
	}

	protected List<SolicitudDespachoLineaDto> asLineas(SolicitudClienteDto entity) {
		val lineas = new ArrayList<SolicitudDespachoLineaDto>();
		int i = 0;
		for (val external : entity.getLineas()) {
			val model = asLinea(i++, external);
			lineas.add(model);
		}
		return lineas;
	}

	protected SolicitudDespachoLineaDto asLinea(int numeroLinea, SolicitudClienteLineaDto external) {
		// @formatter:off
			val result = SolicitudDespachoLineaDto
					.builder()
					.numeroLinea(numeroLinea)
					.numeroLineaExterno(external.getNumeroLineaExterno())
					.numeroSubLineaExterno(external.getNumeroSubLineaExterno())
					.productoCodigoAlterno(external.getProductoCodigoAlterno())
					.productoNombre(external.getProductoNombre())
					.cantidad(external.getCantidad())
					.bodegaCodigoAlterno(external.getBodegaCodigoAlterno())
					.estadoInventarioCodigoAlterno(external.getBodegaCodigoAlterno())
					.lote("")
					.predistribucion(external.getPredistribucion())
					.valorUnitarioDeclarado(null)
					.build();
		// @formatter:on
		return result;
	}

	protected ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(ActualizacionIntegracionDto entry,
			String attribute) {
		val format = "El atributo %s no admite valores nulos o vacios. Debe sumnistrar un valor.";
		val mensaje = String.format(format, attribute);
		val result = getErroresService().error(entry, attribute, mensaje);
		return result;
	}
}
