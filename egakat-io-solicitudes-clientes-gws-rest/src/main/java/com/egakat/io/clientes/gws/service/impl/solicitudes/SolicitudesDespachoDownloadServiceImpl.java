package com.egakat.io.clientes.gws.service.impl.solicitudes;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.service.impl.rest.RestIntegracionEntityDownloadServiceImpl;
import com.egakat.io.clientes.gws.components.GwsRestClient;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.RestConstants;
import com.egakat.io.clientes.gws.dto.SolicitudDespachoClienteDto;
import com.egakat.io.clientes.gws.dto.SolicitudDespachoClienteLineaDto;
import com.egakat.io.clientes.gws.properties.SolicitudesDespachoRestProperties;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoDownloadService;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class SolicitudesDespachoDownloadServiceImpl
		extends RestIntegracionEntityDownloadServiceImpl<SolicitudDespachoClienteDto, SolicitudDespachoDto, Object>
		implements SolicitudesDespachoDownloadService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private SolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

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
		return RestConstants.SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "";
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.NO_PROCESADO;
		val subestado = "";

		val result = getActualizacionesService()
				.findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(getIntegracion(), estado, subestado);
		return result;
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

	private ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(ActualizacionDto entry, String attribute) {
		val format = "El atributo %s no admite valores nulos o vacios. Debe sumnistrar un valor.";
		val mensaje = String.format(format, attribute);
		val result = getErroresService().error(entry, attribute, mensaje);
		return result;
	}

	private ErrorIntegracionDto errorSolicitudNoContieneLineas(ActualizacionDto entry) {
		val mensaje = "La solicitud no contiene líneas. Debe suministrar al menos una línea.";
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

	@Override
	protected SolicitudDespachoDto asOutput(SolicitudDespachoClienteDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val prefijo = defaultString(input.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(input.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;

		val fechaCreacionExterna = LocalDateTime.now();

		val result = new SolicitudDespachoDto();

		result.setIntegracion(actualizacion.getIntegracion());
		result.setCorrelacion(actualizacion.getCorrelacion());
		result.setIdExterno(actualizacion.getIdExterno());

		result.setClienteCodigoAlterno(defaultString(input.getClienteCodigoAlterno()));
		result.setServicioCodigoAlterno(defaultString(input.getServicioCodigoAlterno()));
		result.setNumeroSolicitud(numeroSolicitud);
		result.setPrefijo(prefijo);
		result.setNumeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo);
		result.setFemi(input.getFemi());
		result.setFema(input.getFema());
		result.setHomi(input.getHomi());
		result.setHoma(input.getHoma());
		result.setRequiereTransporte(requiereTransporte);
		result.setRequiereAgendamiento(requiereAgendamiento);
		result.setRequiereDespacharCompleto(requiereDespacharCompleto);
		result.setTerceroIdentificacion(defaultString(input.getTerceroIdentificacion()));
		result.setTerceroNombre(defaultString(input.getTerceroNombre()));
		result.setCanalCodigoAlterno(defaultString(input.getCanalCodigoAlterno()));
		result.setCiudadCodigoAlterno(defaultString(input.getCiudadCodigoAlterno()));
		result.setDireccion(defaultString(input.getDireccion()));
		result.setPuntoCodigoAlterno(defaultString(input.getPuntoCodigoAlterno()));
		result.setPuntoNombre("");
		result.setAutorizadoIdentificacion("");
		result.setAutorizadoNombres("");
		result.setNumeroOrdenCompra(defaultString(input.getNumeroOrdenCompra()));
		result.setNota(defaultString(input.getNota()));
		result.setFechaCreacionExterna(fechaCreacionExterna);
		result.setLineas(asLineas(input));

		return result;
	}

	private List<SolicitudDespachoLineaDto> asLineas(SolicitudDespachoClienteDto input) {
		val result = new ArrayList<SolicitudDespachoLineaDto>();
		int i = 0;
		for (val e : input.getLineas()) {
			val model = asLinea(i++, e);
			result.add(model);
		}
		return result;
	}

	private SolicitudDespachoLineaDto asLinea(int numeroLinea, SolicitudDespachoClienteLineaDto input) {
		val result = new SolicitudDespachoLineaDto();

		result.setNumeroLinea(numeroLinea);
		result.setNumeroLineaExterno(input.getNumeroLineaExterno());
		result.setNumeroSubLineaExterno(input.getNumeroSubLineaExterno());
		result.setProductoCodigoAlterno(input.getProductoCodigoAlterno());
		result.setProductoNombre(input.getProductoNombre());
		result.setCantidad(input.getCantidad());
		result.setBodegaCodigoAlterno(input.getBodegaCodigoAlterno());
		result.setEstadoInventarioCodigoAlterno(input.getBodegaCodigoAlterno());
		result.setLote("");
		result.setPredistribucion(input.getPredistribucion());
		result.setValorUnitarioDeclarado(null);

		return result;
	}

	@Override
	protected Object push(SolicitudDespachoDto output, SolicitudDespachoClienteDto input,
			ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return null;
	}

	@Override
	protected void onSuccess(Object result, SolicitudDespachoDto output, SolicitudDespachoClienteDto input,
			ActualizacionDto actualizacion) {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;
		val subestado = "";

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(Object result, SolicitudDespachoDto output, SolicitudDespachoClienteDto input,
			ActualizacionDto actualizacion) {
		getCrudService().create(output, actualizacion, actualizacion.getEstadoIntegracion());
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estado = EstadoIntegracionType.ERROR_ESTRUCTURA;

		actualizacion.setEstadoIntegracion(estado);
	}
}
