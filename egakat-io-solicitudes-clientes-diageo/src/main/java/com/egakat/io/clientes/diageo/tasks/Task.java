package com.egakat.io.clientes.diageo.tasks;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.enums.EstadoNotificacionType;
import com.egakat.integration.service.api.crud.ActualizacionCrudService;
import com.egakat.integration.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.clientes.diageo.service.api.DiageoSolicitudDespachoMapService;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	private static final int INDEX_NUMERO_SOLICITUD = 0;
	private static final int INDEX_NUMERO_ORDEN_COMPRA = 1;
	private static final int INDEX_TERCERO_IDENTIFICACION = 2;
	private static final int INDEX_TERCERO_NOMBRE = 3;
	private static final int INDEX_DIRECCION = 4;
	private static final int INDEX_CIUDAD = 5;

	private static final int INDEX_FEMI = 6;
	private static final int INDEX_FEMA = 7;
	private static final int INDEX_BODEGA = 8;
	private static final int INDEX_PRODUCTO = 9;
	private static final int INDEX_CANTIDAD = 10;

	@Autowired
	private DiageoSolicitudDespachoMapService mapService;

	@Value("${directories.inputs}")
	private String inputs;

	@Value("${directories.errors}")
	private String errors;

	@Value("${directories.backups}")
	private String backups;

	@Value("${cron-retries}")
	private Integer retries = 10;

	@Value("${cron-delay-between-retries}")
	private Long delayBetweenRetries = 10L * 1000L;

	@Scheduled(cron = "${cron.solicitudes}")
	public void run() {
		for (int i = 0; i < retries; i++) {
			log.debug("INTEGRACION {}: intento {} de {}", "DIAGEO", i + 1, retries);
			boolean success = true;
			read();
			success &= mapService.map();

			if (success) {
				break;
			} else {
				sleep();
			}
		}
	}

	private void read() {

		try (Stream<Path> walk = Files.walk(Paths.get(inputs))) {

			List<String> files = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			val ts = formatter.format(LocalDateTime.now());
			val directory = Paths.get(backups, ts.substring(0, 6), ts.substring(0, 8));
			createDirectoryIfNotExists(directory);

			for (String file : files) {
				val source = Paths.get(file);
				val target = Paths.get(directory.toString(), ts + "-" + source.getFileName().toString());
				val error = Paths.get(errors, ts + "-" + source.getFileName().toString());

				val data = readFile(file);
				loadData(source.getFileName().toString(), data);
				try {
					Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					Files.move(source, error, StandardCopyOption.REPLACE_EXISTING);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean createDirectoryIfNotExists(final Path directory) {
		boolean result = true;
		if (Files.notExists(directory)) {
			try {
				Files.createDirectories(directory);
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	private List<List<String>> readFile(String csvFile) throws IOException {
		val result = new ArrayList<List<String>>();
		String line = "";
		String cvsSplitBy = ",";

		boolean sw = true;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {
				if (sw) {
					sw = false;
					continue;
				}
				// use comma as separator
				String[] data = line.split(cvsSplitBy);

				val record = new ArrayList<String>();
				for (String value : data) {
					String s = value.trim();
					if (s.startsWith("\"")) {
						s = s.substring(1);
					}
					if (s.endsWith("\"")) {
						s = s.substring(0, s.length() - 1);
					}

					record.add(s);
				}
				result.add(record);
			}
		}
		return result;
	}

	@Autowired
	private ActualizacionCrudService actualizacionesService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Transactional(readOnly = false)
	private void loadData(String correlacion, List<List<String>> data) {

		val mapa = new LinkedHashMap<String, List<List<String>>>();
		for (List<String> record : data) {
			val id_externo = record.get(INDEX_NUMERO_SOLICITUD);

			if (!mapa.containsKey(id_externo)) {
				mapa.put(id_externo, new ArrayList<>());
			}
			mapa.get(id_externo).add(record);
		}

		for (val entry : mapa.entrySet()) {
			try {
				val solicitud = asOutput(correlacion, entry.getKey(), entry.getValue());
				val actualizacion = asModel(correlacion, solicitud);

				val exists = actualizacionesService.exists(actualizacion.getIntegracion(),
						actualizacion.getIdExterno());
				actualizacionesService.enqueue(actualizacion);
				if (!exists) {
					crudService.create(solicitud);
				}
			} catch (Exception e) {
				erroresService.create(getIntegracion(), correlacion, "", e);
			}
		}
	}

	protected ActualizacionDto asModel(String correlacion, SolicitudDespachoDto input) {
		val result = new ActualizacionDto();

		result.setIntegracion(getIntegracion());
		result.setCorrelacion(correlacion);
		result.setIdExterno(input.getNumeroSolicitud());
		result.setEstadoIntegracion(EstadoIntegracionType.ESTRUCTURA_VALIDA);
		result.setSubEstadoIntegracion("");
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);

		return result;
	}

	protected SolicitudDespachoDto asOutput(String correlacion, String id, List<List<String>> input) {
		val record = input.get(0);
		val prefijo = "";
		val numeroSolicitudSinPrefijo = id;
		val numeroSolicitud = id;

		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;

		val fechaCreacionExterna = LocalDateTime.now();

		val result = new SolicitudDespachoDto();

		result.setIntegracion(getIntegracion());
		result.setCorrelacion(correlacion);
		result.setIdExterno(id);

		result.setClienteCodigoAlterno("DIAGEO");
		result.setServicioCodigoAlterno("SALIDA");
		result.setNumeroSolicitud(numeroSolicitud);
		result.setPrefijo(prefijo);
		result.setNumeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo);
		result.setFemi(asDate(record.get(INDEX_FEMI)));
		result.setFema(asDate(record.get(INDEX_FEMA)));
		result.setHomi(LocalTime.of(8, 0));
		result.setHoma(LocalTime.of(17, 0));
		result.setRequiereTransporte(requiereTransporte);
		result.setRequiereAgendamiento(requiereAgendamiento);
		result.setRequiereDespacharCompleto(requiereDespacharCompleto);
		result.setTerceroIdentificacion(defaultString(record.get(INDEX_TERCERO_IDENTIFICACION)));
		result.setTerceroNombre(defaultString(record.get(INDEX_TERCERO_NOMBRE)));
		result.setCanalCodigoAlterno("PREDETERMINADO");
		result.setCiudadCodigoAlterno(defaultString(record.get(INDEX_CIUDAD)));
		result.setDireccion(defaultString(record.get(INDEX_DIRECCION)));
		result.setPuntoCodigoAlterno("");
		result.setPuntoNombre("");
		result.setAutorizadoIdentificacion("");
		result.setAutorizadoNombres("");
		result.setTipoDocumentoCodigoOrdenCompra("OCCF");
		result.setNumeroOrdenCompra(defaultString(record.get(INDEX_NUMERO_ORDEN_COMPRA)));
		result.setNota("");

		result.setContactoPrincipalNombre("");
		result.setContactoPrincipalTelefono("");
		result.setContactoSecundarioNombre("");
		result.setContactoSecundarioTelefono("");

		result.setFechaCreacionExterna(fechaCreacionExterna);
		result.setLineas(asLineas(input));

		return result;
	}

	private LocalDate asDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(date, formatter);
	}

	private List<SolicitudDespachoLineaDto> asLineas(List<List<String>> input) {
		val result = new ArrayList<SolicitudDespachoLineaDto>();
		int i = 0;
		for (val e : input) {
			val model = asLinea(i++, e);
			result.add(model);
		}
		return result;
	}

	private SolicitudDespachoLineaDto asLinea(int numeroLinea, List<String> input) {
		val result = new SolicitudDespachoLineaDto();

		result.setNumeroLinea(numeroLinea);
		result.setNumeroLineaExterno("" + numeroLinea);
		result.setNumeroSubLineaExterno("");
		result.setProductoCodigoAlterno(input.get(INDEX_PRODUCTO));
		result.setProductoNombre("");
		result.setCantidad(Integer.parseInt(input.get(INDEX_CANTIDAD)));
		result.setBodegaCodigoAlterno(input.get(INDEX_BODEGA));
		result.setEstadoInventarioCodigoAlterno("A");
		result.setLote("");
		result.setPredistribucion("");
		result.setValorUnitarioDeclarado(null);

		return result;
	}

	private String getIntegracion() {
		return "DIAGEO_SOLICITUDES_DESPACHO";
	}

	protected String defaultCorrelacion() {
		val result = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
		return result;
	}

	private void sleep() {
		try {
			Thread.sleep(delayBetweenRetries * 1000);
		} catch (InterruptedException e) {
			;
		}
	}
}
