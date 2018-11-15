package com.egakat.io.gws.service.impl.solicitudes;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.service.impl.rest.RestDownloadServiceImpl;
import com.egakat.io.gws.client.components.GwsRestClient;
import com.egakat.io.gws.client.constants.RestConstants;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.constants.IntegracionesConstants;
import com.egakat.io.gws.dto.SolicitudDespachoClienteDto;
import com.egakat.io.gws.dto.SolicitudDespachoClienteLineaDto;
import com.egakat.io.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;

import lombok.val;

@Service
public class SolicitudesDespachoDownloadServiceImpl
		extends RestDownloadServiceImpl<SolicitudDespachoClienteDto, SolicitudDespachoDto>
		implements SolicitudesDespachoDownloadService {

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "";
	}

	@Override
	protected SolicitudDespachoClienteDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getUrl();

		val response = getRestClient().get(url, SolicitudDespachoClienteDto.class, actualizacion.getIdExterno());
		val result = response.getBody();
		return result;
	}

	@Override
	protected void validateInput(SolicitudDespachoClienteDto input, ActualizacionDto actualizacion,
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

		if (input.getLineas().isEmpty()) {
			errores.add(errorSolicitudNoContieneLineas(actualizacion));
		} else {
			val lineas = input.getLineas().stream().filter(a -> a.getCantidad() <= 0).collect(Collectors.toList());
			for (val linea : lineas) {
				errores.add(errorCantidadSolicitadaInconsistente(actualizacion, linea));
			}
		}

	}

	@Override
	protected SolicitudDespachoDto asModel(SolicitudDespachoClienteDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
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

	protected ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(ActualizacionDto entry, String attribute) {
		val format = "El atributo %s no admite valores nulos o vacios. Debe sumnistrar un valor.";
		val mensaje = String.format(format, attribute);
		val result = getErroresService().error(entry, attribute, mensaje);
		return result;
	}

	protected ErrorIntegracionDto errorSolicitudNoContieneLineas(ActualizacionDto entry) {
		val mensaje = "La solicitud no contiene lineas. Debe sumnistrar al menos una línea.";
		val result = getErroresService().error(entry, "líneas", mensaje);
		return result;
	}

	private ErrorIntegracionDto errorCantidadSolicitadaInconsistente(ActualizacionDto actualizacion,
			SolicitudDespachoClienteLineaDto linea) {
		val format = "En la línea con número %s y código de artículo %s, se estan solicitando %d unidades. Debe solicitar una cantidad mayor que cero";
		val mensaje = String.format(format, linea.getNumeroLineaExterno(), linea.getProductoCodigoAlterno(),
				linea.getCantidad());
		val result = getErroresService().error(actualizacion, "líneas", mensaje);
		return result;
	}
}
