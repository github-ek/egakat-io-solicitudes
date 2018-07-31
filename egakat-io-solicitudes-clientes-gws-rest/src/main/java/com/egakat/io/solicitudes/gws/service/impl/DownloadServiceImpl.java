package com.egakat.io.solicitudes.gws.service.impl;

import static com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto.error;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ERROR_ESTRUCTURA;
import static com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType.ESTRUCTURA_VALIDA;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto;
import com.egakat.io.solicitudes.gws.service.api.DownloadService;
import com.egakat.io.solicitudes.gws.service.api.client.SalidasLocalService;
import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.SolicitudDespachoCrudService;
import com.gws.integraciones.solicitudes.salidas.dto.SolicitudDto;

import lombok.val;

@Service
public class DownloadServiceImpl implements DownloadService {

	@Autowired
	private SalidasLocalService externalService;

	@Autowired
	private SolicitudDespachoCrudService localService;

	@Autowired
	private EntradaIntegracionCrudService entradasService;

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Override
	public void download(EntradaIntegracionDto entry) {
		SolicitudDto externalEntity = null;
		SolicitudDespachoDto localEntity = null;
		val errores = new ArrayList<ErrorIntegracionDto>();

		try {
			externalEntity = getExternalEntity(entry, errores);
			if (errores.isEmpty()) {
				localEntity = asLocalEntity(externalEntity, entry, errores);
			}
		} catch (RuntimeException e) {
			val error = error(entry.getIntegracion(), entry.getIdExterno(), "", e);
			errores.add(error);
		}

		if (errores.isEmpty()) {
			createLocalEntity(localEntity);
			entry.setEstado(ESTRUCTURA_VALIDA);
		} else {
			if(externalEntity !=null) {
				for (val error : errores) {
					error.setArg0(externalEntity.getSeriesName());
					error.setArg1(String.valueOf(externalEntity.getDocNum()));
				}
			}
			erroresService.create(errores);
			entry.setEstado(ERROR_ESTRUCTURA);
		}
		entry.setProgramarNotificacion(true);
		entry.setNotificacionRealizada(false);
		entradasService.update(entry);
	}

	protected SolicitudDto getExternalEntity(EntradaIntegracionDto entry, List<ErrorIntegracionDto> errores) {
		val id = Integer.parseInt(entry.getIdExterno());
		val result = externalService.findOneById(id);
		return result;
	}

	protected SolicitudDespachoDto asLocalEntity(SolicitudDto entity, EntradaIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {

		val idCorrelacion = String.format("%s-%s", entry.getIntegracion(), LocalDate.now().toString());
		val idExterno = String.valueOf(entity.getId());

		val prefijo = defaultString(entity.getSeriesName());
		val numeroSolicitudSinPrefijo = String.valueOf(entity.getDocNum());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val femi = getFecha(entity.getFeMi(), "femi", entry, errores);
		val fema = getFecha(entity.getFeMa(), "fema", entry, errores);
		val homi = getHora(entity.getHoMi(), "homi", entry, errores);
		val homa = getHora(entity.getHoMa(), "homa", entry, errores);

		val requiereAgendamiento = "N";
		val requiereTransporte = "S";
		val requiereDespacharCompleto = "N";

		// TODO NO HAY ORDEN COMPRA, CUANDO ES OBLIGATORIO
		val numeroOrdenCompra = defaultString("");
		LocalDate fechaOrdenCompra = null;

		val nota = defaultString(entity.getComments());

		val fechaCreacionExterna = LocalDateTime.now();

		SolicitudDespachoDto result = null;

		if (errores.isEmpty()) {
			// @formatter:off
			result = SolicitudDespachoDto
					.builder() 
					.idCorrelacion(idCorrelacion)
					.integracion(entry.getIntegracion())
					.idExterno(idExterno)
					.estado(ESTRUCTURA_VALIDA)
					.clienteCodigoAlterno(defaultString(entity.getCodCliente()))
					.servicioCodigoAlterno(defaultString(entity.getTipoServicio()))
					.numeroSolicitud(numeroSolicitud)
					.prefijo(prefijo)
					.numeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo)
					.femi(femi)
					.fema(fema)
					.homi(homi)
					.homa(homa)
					.requiereAgendamiento(requiereAgendamiento)
					.requiereTransporte(requiereTransporte)
					.requiereDespacharCompleto(requiereDespacharCompleto)
					.terceroIdentificacion(defaultString(entity.getNit()))
					.terceroNombre(defaultString(entity.getRazonSocial()))
					.canalCodigoAlterno(defaultString(entity.getGroupName()))
					.ciudadCodigoAlterno(defaultString(entity.getCodDane()))
					.direccion(defaultString(entity.getDireccion()))
					.puntoCodigoAlterno("")
					.puntoNombre("")
					.autorizadoIdentificacion("")
					.autorizadoNombres("")
					.numeroOrdenCompra(numeroOrdenCompra)
					.fechaOrdenCompra(fechaOrdenCompra)
					.nota(nota)
					.fechaCreacionExterna(fechaCreacionExterna)
					.build();
			// @formatter:on
		}
		return result;
	}

	protected void createLocalEntity(final com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto localEntity) {
		localService.create(localEntity);
	}

	@Override
	public void acknowledge(EntradaIntegracionDto entry) {
		// TODO Auto-generated method stub

	}

	protected LocalDate getFecha(LocalDate value, String attribute, EntradaIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {
		LocalDate result = null;

		if (value != null) {
			result = value;
		} else {
			val msg = "No se ha suministrado un valor para este atributo de tipo FECHA.EN PRODUCCION SIEMPRE DEBE ENVIAR UNA FECHA A MENOS QUE EXPLICITAMENTE EXISTA UNA REGLA PARA DEDUCIRLA Y DEPENDA DEL OPL";
			val error = error(entry.getIntegracion(), entry.getIdExterno(), attribute, msg);
			errores.add(error);
		}

		return result;
	}

	private LocalTime getHora(String value, String attribute, EntradaIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {
		LocalTime result = null;

		if (value != null) {
			val formatter = getDateTimeFormatter();
			try {
				result = LocalTime.parse(StringUtils.left(value, 5), formatter);
			} catch (DateTimeParseException e) {
				val error = error(entry.getIntegracion(), entry.getIdExterno(), attribute, e);
				errores.add(error);
			}
		} else {
			val msg = "No se ha suministrado un valor para este atributo de tipo HORA.EN PRODUCCION SIEMPRE DEBE ENVIAR UNA FECHA A MENOS QUE EXPLICITAMENTE EXISTA UNA REGLA PARA DEDUCIRLA Y DEPENDA DEL OPL";
			val error = error(entry.getIntegracion(), entry.getIdExterno(), attribute, msg);
			errores.add(error);
		}

		return result;
	}

	private static DateTimeFormatter formatter = null;

	protected static DateTimeFormatter getDateTimeFormatter() {
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern("HH:mm");
		}
		return formatter;
	}

}
