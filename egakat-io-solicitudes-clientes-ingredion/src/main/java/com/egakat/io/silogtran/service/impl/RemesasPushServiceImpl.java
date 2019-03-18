package com.egakat.io.silogtran.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.io.ingredion.dto.ActaDto;
import com.egakat.io.ingredion.dto.ErrorDto;
import com.egakat.io.ingredion.service.api.ActasIngredionAlistadasService;
import com.egakat.io.silogtran.components.SilogtranRestClient;
import com.egakat.io.silogtran.components.SilogtranRestProperties;
import com.egakat.io.silogtran.components.SilogtranTokenGenerator;
import com.egakat.io.silogtran.constants.SilogtranRestConstants;
import com.egakat.io.silogtran.dto.RemesaDto;
import com.egakat.io.silogtran.dto.RemesaItemDto;
import com.egakat.io.silogtran.service.api.RemesasPushService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemesasPushServiceImpl extends RestPushServiceImpl<Long, RemesaDto, RemesaDto, String>
		implements RemesasPushService {

	private static final String EXPIRED_TOKEN = "expired token";

	private static final String FIELD_MSG = "msg";

	@Value("${com.silogtran.rest.api-secret-value}")
	private String apiSecretValue;

	@Autowired
	private ActasIngredionAlistadasService service;

	@Autowired
	private SilogtranTokenGenerator tokenGenerator;

	@Autowired
	private SilogtranRestProperties properties;

	@Autowired
	private SilogtranRestClient restClient;

	private ObjectMapper mapper;

	@Override
	protected SilogtranRestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getIntegracion() {
		return "REMESAS SILOGTRAN";
	}

	@Override
	protected String getApiEndPoint() {
		return SilogtranRestConstants.postRemesas;
	}

	@Override
	protected List<RemesaDto> getPendientes() {
		LocalDate fechaDesde = LocalDate.of(2019, 1, 1);
		LocalDate fechaHasta = LocalDate.of(2020, 1, 1);
		// List<String> estados = Arrays.asList("ORDEN_ALISTAMIENTO_EN_STAGE");
		List<String> estados = Arrays.asList("PROCESADO");
		List<String> bodegas = new ArrayList<>();

		List<RemesaDto> result = new ArrayList<>();
		val actas = service.getActasAlistadas(fechaDesde, fechaHasta, estados, bodegas);

		val grupos = getGruposByIdSolicitudActa(actas);

		int i = 0;
		int n = grupos.keySet().size();

		for (val id : grupos.keySet()) {

			val lineas = grupos.get(id);
			RemesaDto remesa = asRemesa(lineas.get(0));
			for (val linea : lineas) {
				remesa.getItems().add(asRemesaItem(linea));
			}

			result.add(remesa);

			log.debug("{} de {}: {}", i++, n, remesa.toString());
		}

		return result;
	}

	@Override
	protected void init(List<RemesaDto> inputs) {
		this.mapper = new ObjectMapper();
	}

	@Override
	protected RemesaDto asOutput(RemesaDto input, List<ErrorDto> errores) {
		return input;
	}

	@Override
	protected String push(RemesaDto output, RemesaDto input, List<ErrorDto> errores) {
		try {
			String result = null;
			val url = getUrl();

			ObjectNode body = asBodyRequest(output);

			log.debug("body={}", body);
			val response = getRestClient().post(url, body, String.class);

			result = readResponse(response.getBody(), errores);

			input.setNumeroConfirmacionSilogtran(result);
			input.setFechaIntegracionSilogtran(LocalDateTime.now());

			return result;
		} catch (HttpStatusCodeException e) {
			if (cacheEvict(e)) {
				tokenGenerator.cacheEvict();
			}
			throw e;
		}
	}

	private boolean cacheEvict(HttpStatusCodeException e) {
		boolean result = false;

		if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
			result = true;
		} else {
			if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
				try {
					val body = e.getResponseBodyAsString();
					val node = this.mapper.readTree(body).findValue(FIELD_MSG);

					if (node != null) {
						val msg = node.asText();
						if (EXPIRED_TOKEN.equalsIgnoreCase(msg)) {
							result = true;
						}
					}
				} catch (IOException e1) {
					;
				}
			}
		}
		
		return result;
	}

	@Override
	protected void onSuccess(String result, RemesaDto output, RemesaDto input) {
		output.setEstadoIntegracion("REMESA_ENVIADA");
		output.setSubEstadoIntegracion("");
	}

	@Override
	protected void updateOnSuccess(String result, RemesaDto output, RemesaDto input) {
		service.marcarActasEnvidas(output);
	}

	@Override
	protected void onError(RemesaDto input, List<ErrorDto> errores) {
		input.setEstadoIntegracion("ERROR_ENVIO");
		input.setSubEstadoIntegracion("");
	}

	@Override
	protected void updateOnError(RemesaDto input, List<ErrorDto> errores) {
		service.errorDuranteEnvio(input, errores);
	}

	@Override
	protected void updateOnRetry(RemesaDto input, List<ErrorDto> errores) {
		service.errorDuranteEnvio(input, errores);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// --
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static DecimalFormat df = new DecimalFormat("0.0", getDecimalFormatSymbols());

	private static DecimalFormatSymbols getDecimalFormatSymbols() {
		val result = new DecimalFormatSymbols(Locale.getDefault());
		result.setDecimalSeparator('.');
		result.setGroupingSeparator(',');
		return result;
	}

	protected Map<Long, List<ActaDto>> getGruposByIdSolicitudActa(List<ActaDto> request) {
		val comparator = Comparator.comparing(ActaDto::getIdSolicitudActa);
		val groupingBy = Collectors.groupingBy(ActaDto::getIdSolicitudActa);

		val result = request.stream().sorted(comparator).collect(groupingBy);
		return result;
	}

	private RemesaDto asRemesa(ActaDto acta) {
		val result = new RemesaDto();

		result.setId(acta.getId());
		result.setIdExterno(acta.getIdExterno());
		result.setEstadoIntegracion(acta.getEstadoIntegracion());
		result.setSubEstadoIntegracion(acta.getSubEstadoIntegracion());
		result.setReintentos(acta.getReintentos());
		result.setFechaCreacion(acta.getFechaCreacion());
		result.setFechaModificacion(acta.getFechaModificacion());

		result.setCentroCosto(acta.getCentroCosto());
		result.setTipoRemesa(acta.getTipoRemesa());
		result.setCodigoCliente(acta.getClienteCodigoAlternoTms());
		result.setDivisionCliente(acta.getClienteDivision());

		result.setNombreRemitente(acta.getRemitenteNombre());
		result.setTipoDocumentoRemitente(acta.getRemitenteTipoIdentificacion());
		result.setDocumentoRemitente(acta.getRemitenteIdentificacion());
		result.setDireccionRemitente(acta.getRemitenteDireccion());
		result.setTelefonoRemitente(acta.getRemitenteTelefono());
		result.setContactoRemitente(acta.getRemitenteContacto());
		result.setCiudadRemitente(acta.getRemitenteCiudad());
		result.setDepartamentoRemitente(acta.getRemitenteDepartamento());

		result.setNombreDestinatario(acta.getDestinatarioNombre());
		result.setTipoDocumentoDestinatario(acta.getDestinatarioTipoIdentificacion());
		result.setDocumentoDestinatario(acta.getDestinatarioIdentificacion());
		result.setDireccionDestinatario(acta.getDestinatarioDireccion());
		result.setTelefonoDestinatario(acta.getDestinatarioTelefono());
		result.setContactoDestinatario1(acta.getResponsablePrincipal());
		result.setContactoDestinatario2(acta.getResponsableSuplente());
		result.setCiudadDestinatario(acta.getDestinatarioCiudad());
		result.setDepartamentoDestinatario(acta.getDestinatarioDepartamento());

		result.setZonaCiudadDestinatario(acta.getDestinatarioCiudadZona());
		acta.getDestinatarioCoordenadaX().toPlainString();
		result.setCoordenadaXLongitud(df.format(acta.getDestinatarioCoordenadaX()));
		result.setCoordenadaYLatitud(df.format(acta.getDestinatarioCoordenadaY()));

		result.setObservacionRemesa(acta.getRemesaObservacion());

		result.setFechaCompromisoMinima(acta.getFechaCompromisoInicial());
		result.setFechaCompromisoMaxima(acta.getFechaCompromisoFinal());
		result.setHoraCompromisoMinima(acta.getHoraCompromisoInicial());
		result.setHoraCompromisoMaxima(acta.getHoraCompromisoFinal());

		result.setPlacaVehiculo(acta.getPlacaVehiculo());
		result.setSecuenciaEntrega(acta.getSecuenciaEntrega());

		result.setTipoRemision(acta.getRemisionTipoRemision());
		result.setRemision(acta.getRemisionNumeroDocumento());
		result.setDocumentoWms(acta.getOrdenAlistamientoNumeroDocumento());
		result.setDocumentoNumeroSolicitud(acta.getSolicitudNumeroDocumento());
		// TODO se debe usar la tabla de ordenes de transporte
		result.setIdOrdentransporte(acta.getIdSolicitudActa());

		result.setPuntoCodigoAlterno(acta.getPuntoCodigoAlterno());
		result.setRegional(acta.getRegional());
		result.setCiudadNombreAlterno(acta.getCiudadNombreAlterno());
		result.setBodegaCodigoAlterno(acta.getBodegaCodigoAlterno());
		result.setPrograma(acta.getPrograma());
		result.setPlanta(acta.getPlanta());

		result.setItems(new ArrayList<>());
		return result;
	}

	private RemesaItemDto asRemesaItem(ActaDto acta) {
		val result = new RemesaItemDto();
		result.setIdProductoEconnect(String.valueOf(acta.getIdProducto()));
		result.setCodigoProducto(acta.getProductoCodigo());
		result.setProductoNombre(acta.getProductoNombre());
		result.setProductoCodigoministerio("702");
		result.setNaturalezaCarga("NORMAL");
		result.setTipoProducto("PRODUCTOS ALIMENTICIOS");
		result.setCodigoEmpaque(acta.getUnidadEmpaqueCodigoAlternoTms());
		result.setPeso(df.format(acta.getPesoEmpaques()));
		result.setPesoBruto(df.format(acta.getPesoBrutoEmpaques()));
		result.setCantidad(df.format(acta.getCantidad()));
		result.setVolumen(df.format(acta.getVolumenEmpaques()));
		result.setValorDeclarado(String.valueOf(acta.getValorDeclarado()));
		result.setDescripcionDetalleRemesa(acta.getRemesaObservacion());
		result.setPredistribucion(acta.getPredistribucion());
		result.setFactorConversion(String.valueOf(acta.getFactorConversion()));
		result.setCantidadEmbalaje(df.format(acta.getCantidad()));
		result.setLote(acta.getLote());
		result.setEstadoInventarioNombre(acta.getEstadoInventarioNombre());
		result.setFechaVencimiento(acta.getFechaVencimiento());

		return result;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// --
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private ObjectNode asBodyRequest(RemesaDto output) {
		ObjectNode body;
		body = this.mapper.createObjectNode();
		JsonNode remesa = this.mapper.convertValue(output, JsonNode.class);

		body.put("secret", apiSecretValue);
		body.putArray("remesas").add(remesa);
		return body;
	}

	private String readResponse(String response, List<ErrorDto> errores) {
		String result = "";

		try {
			JsonNode node = this.mapper.readTree(response);

			val remesas = node.get("remesas");

			if (remesas != null) {
				result = node.get("numero_confirmacion").asText();

				readMessages(remesas, errores);
			} else {
				errorRespuestaNoEsperada(node, response, errores);
			}
		} catch (IOException e) {
			errores.add(error(e.getClass().getName(), e.getMessage()));
		}

		return result;
	}

	private void readMessages(JsonNode remesas, List<ErrorDto> errores) {
		val remesa = remesas.get(0);
		val success = remesa.get("success").asBoolean();
		if (!success) {
			remesa.get("msg").forEach(errorMapper(errores));
		}
	}

	private void errorRespuestaNoEsperada(JsonNode node, String response, List<ErrorDto> errores) {
		val msg = node.get("msg");
		if (msg != null) {
			errores.add(error("", msg.asText()));
		} else {
			errores.add(error("Respuesta no valida", response));
		}
	}

	private Consumer<? super JsonNode> errorMapper(List<ErrorDto> errores) {
		return a -> {
			val codigo = StringUtils.defaultString(a.get("codigo").asText());
			val mensaje = StringUtils.defaultString(a.get("mensaje").asText());

			errores.add(error(codigo, mensaje));
		};
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// --
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private ErrorDto error(String codigo, String mensaje) {
		ErrorDto error;
		error = new ErrorDto();

		val now = LocalDateTime.now();

		error.setCodigo(codigo);
		error.setMensaje(mensaje);
		error.setEstadoNotificacion("NOTIFICAR");
		error.setFechaCreacion(now);
		error.setFechaModificacion(now);
		return error;
	}
}
