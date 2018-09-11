package com.egakat.io.gws.commons.core.service.impl;

import static com.egakat.io.gws.commons.core.enums.EstadoIntegracionType.ESTRUCTURA_VALIDA;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.service.api.DownloadService;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoLineaCrudService;
import com.egakat.io.gws.solicitudes.dto.SolicitudClienteDto;
import com.egakat.io.gws.solicitudes.service.api.SolicitudClienteLocalService;

import lombok.val;

@Service
public class DownloadServiceImpl implements DownloadService {

	@Autowired
	private SolicitudClienteLocalService externalService;

	@Autowired
	private SolicitudDespachoCrudService localService;

	@Autowired
	private SolicitudDespachoLineaCrudService localLineaService;
	
	@Autowired
	private ErrorIntegracionCrudService erroresService;

	protected ErrorIntegracionCrudService getErroresService() {
		return erroresService;
	}

	@Override
	public void download(ActualizacionIntegracionDto entry, List<ErrorIntegracionDto> errores) {
		errores.clear();

		SolicitudClienteDto external = null;
		SolicitudDespachoDto model = null;

		try {
			external = getExternalEntity(entry, errores);
			if (errores.isEmpty()) {
				model = asModel(external, entry, errores);
				if (errores.isEmpty()) {
					create(model);
					entry.setEstadoIntegracion(ESTRUCTURA_VALIDA);
				}
			}
		} catch (RuntimeException e) {
			val error = error(entry, e);
			errores.add(error);
		}

		if (!errores.isEmpty()) {
			discard(external, errores);
		}
	}

	protected SolicitudClienteDto getExternalEntity(ActualizacionIntegracionDto entry, List<ErrorIntegracionDto> errores) {
		val id = Integer.parseInt(entry.getIdExterno());
		val result = externalService.findOneById(id);
		// TODO QUITAR ESTO
		result.setId(id);
		return result;
	}

	protected SolicitudDespachoDto asModel(SolicitudClienteDto external, ActualizacionIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {

		val idExterno = String.valueOf(external.getId());

		val prefijo = defaultString(external.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(external.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val femi = getFecha(external.getFemi(), "femi", entry, errores);
		val fema = getFecha(external.getFema(), "fema", entry, errores);
		val homi = getHora(external.getHomi(), "homi", entry, errores);
		val homa = getHora(external.getHoma(), "homa", entry, errores);

		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;

		LocalDate fechaOrdenCompra = null;
		val fechaCreacionExterna = LocalDateTime.now();
		val lineas = asLineas(external);

		SolicitudDespachoDto result = null;

		if (errores.isEmpty()) {
			// @formatter:off
			result = SolicitudDespachoDto
					.builder() 
					.integracion(entry.getIntegracion())
					.idExterno(idExterno)
					.correlacion(entry.getCorrelacion())
					.clienteCodigoAlterno(defaultString(external.getClienteCodigoAlterno()))
					.servicioCodigoAlterno(defaultString(external.getServicioCodigoAlterno()))
					.numeroSolicitud(numeroSolicitud)
					.prefijo(prefijo)
					.numeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo)
					.femi(femi)
					.fema(fema)
					.homi(homi)
					.homa(homa)
					.requiereTransporte(requiereTransporte)
					.requiereAgendamiento(requiereAgendamiento)
					.requiereDespacharCompleto(requiereDespacharCompleto)
					.terceroIdentificacion(defaultString(external.getTerceroIdentificacion()))
					.terceroNombre(defaultString(external.getTerceroNombre()))
					.canalCodigoAlterno(defaultString(external.getCanalCodigoAlterno()))
					.ciudadCodigoAlterno(defaultString(external.getCiudadCodigoAlterno()))
					.direccion(defaultString(external.getDireccion()))
					.puntoCodigoAlterno(defaultString(external.getPuntoCodigoAlterno()))
					.puntoNombre("")
					.autorizadoIdentificacion("")
					.autorizadoNombres("")
					.numeroOrdenCompra(defaultString(external.getNumeroOrdenCompra()))
					.fechaOrdenCompra(fechaOrdenCompra)
					.nota(defaultString(external.getNota()))
					.fechaCreacionExterna(fechaCreacionExterna)
					.lineas(lineas)
					.build();
			// @formatter:on
		}
		return result;
	}

	protected List<SolicitudDespachoLineaDto> asLineas(SolicitudClienteDto entity) {
		val lineas = new ArrayList<SolicitudDespachoLineaDto>();
		int i = 0;
		for (val external : entity.getLineas()) {
			// @formatter:off
			val model = SolicitudDespachoLineaDto.builder()
					.numeroLinea(i++)
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
			lineas.add(model);
		}
		return lineas;
	}

	protected void create(SolicitudDespachoDto model) {
		val newModel = localService.create(model);

		val id = newModel.getId();
		for (val linea : model.getLineas()) {
			linea.setIdSolicitudDespacho(id);
			localLineaService.create(linea);
		}
	}

	protected void discard(SolicitudClienteDto external, List<ErrorIntegracionDto> errores) {
		if (external != null) {
			for (val error : errores) {
				error.setArg0(external.getClienteCodigoAlterno());
				error.setArg1(external.getPrefijo());
				error.setArg2(external.getNumeroSolicitudSinPrefijo());
				error.setArg3(external.getServicioCodigoAlterno());
				error.setArg4(external.getTerceroIdentificacion());
				error.setArg5(external.getTerceroNombre());
				error.setArg6(external.getCanalCodigoAlterno());
				error.setArg7(external.getCiudadCodigoAlterno());
			}
		}
	}

	protected LocalDate getFecha(LocalDate value, String attribute, ActualizacionIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {
		LocalDate result = null;

		if (value != null) {
			result = value;
		} else {
			val msg = "No se ha suministrado un valor para este atributo de tipo FECHA.EN PRODUCCION SIEMPRE DEBE ENVIAR UNA FECHA A MENOS QUE EXPLICITAMENTE EXISTA UNA REGLA PARA DEDUCIRLA Y DEPENDA DEL OPL";
			val error = error(entry, attribute, msg);
			errores.add(error);
		}

		return result;
	}

	private LocalTime getHora(LocalTime value, String attribute, ActualizacionIntegracionDto entry,
			List<ErrorIntegracionDto> errores) {
		LocalTime result = null;

		if (value != null) {
			result = value;
		} else {
			val msg = "No se ha suministrado un valor para este atributo de tipo HORA.EN PRODUCCION SIEMPRE DEBE ENVIAR UNA HORA A MENOS QUE EXPLICITAMENTE EXISTA UNA REGLA PARA DEDUCIRLA Y DEPENDA DEL OPL";
			val error = error(entry, attribute, msg);
			errores.add(error);
		}

		return result;
	}

	protected ErrorIntegracionDto error(ActualizacionIntegracionDto entry, String codigo, String mensaje) {
		val result = getErroresService().error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(),
				codigo, mensaje);
		return result;
	}

	protected ErrorIntegracionDto error(ActualizacionIntegracionDto entry, Throwable e) {
		val result = getErroresService().error(entry.getIntegracion(), entry.getIdExterno(), entry.getCorrelacion(), "",
				e);
		return result;
	}

	@Override
	public void download() {
		// TODO Auto-generated method stub
		
	}
}
