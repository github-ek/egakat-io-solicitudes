package com.egakat.io.gws.cliente.service.impl.solicitudes;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.service.impl.RestDownloadServiceImpl;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.gws.cliente.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.cliente.configuration.constants.IntegracionesRestConstants;
import com.egakat.io.gws.cliente.configuration.properties.SolicitudesClienteRestProperties;
import com.egakat.io.gws.cliente.dto.SolicitudDespachoClienteDto;
import com.egakat.io.gws.cliente.dto.SolicitudDespachoClienteLineaDto;
import com.egakat.io.gws.cliente.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class SolicitudesDespachoDownloadServiceImpl
		extends RestDownloadServiceImpl<SolicitudDespachoClienteDto, SolicitudDespachoDto>
		implements SolicitudesDespachoDownloadService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private SolicitudesClienteRestProperties properties;

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
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getApiEndPoint() {
		return IntegracionesRestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "/{id}";
	}

	protected Class<SolicitudDespachoClienteDto> getResponseType() {
		return SolicitudDespachoClienteDto.class;
	}

	@Override
	protected SolicitudDespachoClienteDto getInput(ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, getResponseType(), actualizacion.getIdExterno());

		val result = response.getBody();
		return result;
	}

	@Override
	protected void validateInput(SolicitudDespachoClienteDto input, ActualizacionIntegracionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		super.validateInput(input, actualizacion, errores);

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
	protected SolicitudDespachoDto asModel(SolicitudDespachoClienteDto input,
			ActualizacionIntegracionDto actualizacion) {
		val prefijo = defaultString(input.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(input.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;

		val fechaCreacionExterna = LocalDateTime.now();

		val model = new SolicitudDespachoDto();

		model.setIntegracion(actualizacion.getIntegracion());
		model.setCorrelacion(actualizacion.getCorrelacion());
		model.setIdExterno(actualizacion.getIdExterno());

		model.setClienteCodigoAlterno(defaultString(input.getClienteCodigoAlterno()));
		model.setServicioCodigoAlterno(defaultString(input.getServicioCodigoAlterno()));
		model.setNumeroSolicitud(numeroSolicitud);
		model.setPrefijo(prefijo);
		model.setNumeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo);
		model.setFemi(input.getFemi());
		model.setFema(input.getFema());
		model.setHomi(input.getHomi());
		model.setHoma(input.getHoma());
		model.setRequiereTransporte(requiereTransporte);
		model.setRequiereAgendamiento(requiereAgendamiento);
		model.setRequiereDespacharCompleto(requiereDespacharCompleto);
		model.setTerceroIdentificacion(defaultString(input.getTerceroIdentificacion()));
		model.setTerceroNombre(defaultString(input.getTerceroNombre()));
		model.setCanalCodigoAlterno(defaultString(input.getCanalCodigoAlterno()));
		model.setCiudadCodigoAlterno(defaultString(input.getCiudadCodigoAlterno()));
		model.setDireccion(defaultString(input.getDireccion()));
		model.setPuntoCodigoAlterno(defaultString(input.getPuntoCodigoAlterno()));
		model.setPuntoNombre("");
		model.setAutorizadoIdentificacion("");
		model.setAutorizadoNombres("");
		model.setNumeroOrdenCompra(defaultString(input.getNumeroOrdenCompra()));
		model.setNota(defaultString(input.getNota()));
		model.setFechaCreacionExterna(fechaCreacionExterna);
		model.setLineas(asLineas(input));

		return model;
	}

	protected List<SolicitudDespachoLineaDto> asLineas(SolicitudDespachoClienteDto input) {
		val result = new ArrayList<SolicitudDespachoLineaDto>();
		int i = 0;
		for (val e : input.getLineas()) {
			val model = asLinea(i++, e);
			result.add(model);
		}
		return result;
	}

	protected SolicitudDespachoLineaDto asLinea(int numeroLinea, SolicitudDespachoClienteLineaDto input) {
		val model = new SolicitudDespachoLineaDto();

		model.setNumeroLinea(numeroLinea);
		model.setNumeroLineaExterno(input.getNumeroLineaExterno());
		model.setNumeroSubLineaExterno(input.getNumeroSubLineaExterno());
		model.setProductoCodigoAlterno(input.getProductoCodigoAlterno());
		model.setProductoNombre(input.getProductoNombre());
		model.setCantidad(input.getCantidad());
		model.setBodegaCodigoAlterno(input.getBodegaCodigoAlterno());
		model.setEstadoInventarioCodigoAlterno(input.getBodegaCodigoAlterno());
		model.setLote("");
		model.setPredistribucion(input.getPredistribucion());
		model.setValorUnitarioDeclarado(null);

		return model;
	}

	protected ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(ActualizacionIntegracionDto entry,
			String attribute) {
		val format = "El atributo %s no admite valores nulos o vacios. Debe sumnistrar un valor.";
		val mensaje = String.format(format, attribute);
		val result = getErroresService().error(entry, attribute, mensaje);
		return result;
	}
}
