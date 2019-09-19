package com.egakat.io.clientes.ingredion.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trim;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.enums.EstadoNotificacionType;
import com.egakat.integration.service.api.crud.IntegracionEntityCrudService;
import com.egakat.integration.service.impl.PostServiceImpl;
import com.egakat.io.clientes.ingredion.constants.IngredionSolicitudesDespachoConstants;
import com.egakat.io.clientes.ingredion.domain.IngredionSolicitudDespacho;
import com.egakat.io.clientes.ingredion.domain.IngredionSolicitudDespachoLinea;
import com.egakat.io.clientes.ingredion.dto.IngredionSolicitudDespachoDto;
import com.egakat.io.clientes.ingredion.dto.IngredionSolicitudDespachoLineaDto;
import com.egakat.io.clientes.ingredion.dto.SilogtranSolicitudDespachoDto;
import com.egakat.io.clientes.ingredion.repository.IngredionSolicitudDespachoLineaRepository;
import com.egakat.io.clientes.ingredion.repository.IngredionSolicitudDespachoRepository;
import com.egakat.io.clientes.ingredion.service.api.IngredionSolicitudDespachoCrudService;
import com.egakat.io.clientes.ingredion.service.api.IngredionSolicitudDespachoPostService;

import lombok.val;

@Service
public class IngredionSolicitudDespachoPostServiceImpl
		extends PostServiceImpl<SilogtranSolicitudDespachoDto, IngredionSolicitudDespachoDto, Object, Long>
		implements IngredionSolicitudDespachoPostService {

	@Autowired
	private IngredionSolicitudDespachoCrudService crudService;

	@Autowired
	private IngredionSolicitudDespachoRepository solicitudRepository;

	@Autowired
	private IngredionSolicitudDespachoLineaRepository lineaRepository;

	@Override
	protected String getIntegracion() {
		return IngredionSolicitudesDespachoConstants.INTEGRACION_CODIGO;
	}

	protected IntegracionEntityCrudService<IngredionSolicitudDespachoDto> getCrudService() {
		return crudService;
	}

	@Override
	protected ActualizacionDto asActualizacion(String correlacion, SilogtranSolicitudDespachoDto input) {
		val result = new ActualizacionDto();

		result.setIntegracion(getIntegracion());
		result.setCorrelacion(correlacion);
		result.setIdExterno(input.getActa());

		result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
		result.setSubEstadoIntegracion("");
		result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);

		return result;
	}

	@Override
	protected IngredionSolicitudDespachoDto asOutput(SilogtranSolicitudDespachoDto input,
			ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val requiereTransporte = true;
		val requiereAgendamiento = false;
		val requiereDespacharCompleto = false;
		val fechaCreacionExterna = LocalDateTime.now();

		val result = new IngredionSolicitudDespachoDto();

		result.setIntegracion(actualizacion.getIntegracion());
		result.setCorrelacion(actualizacion.getCorrelacion());
		result.setIdExterno(actualizacion.getIdExterno());
		result.setClienteCodigoAlterno(IngredionSolicitudesDespachoConstants.CLIENTE_CODIGO);
		result.setServicioCodigoAlterno(IngredionSolicitudesDespachoConstants.SERVICIO_DESPACHO_CODIGO);
		result.setNumeroSolicitud(trim(defaultString(input.getActa())));
		result.setPrefijo("");
		result.setNumeroSolicitudSinPrefijo(trim(defaultString(input.getActa())));
		result.setFemi(input.getFechaEntregaInicial());
		result.setFema(input.getFechaEntregaFinal());
		result.setHomi(IngredionSolicitudesDespachoConstants.HORA_MINIMA);
		result.setHoma(IngredionSolicitudesDespachoConstants.HORA_MAXIMA);
		result.setRequiereTransporte(requiereTransporte);
		result.setRequiereAgendamiento(requiereAgendamiento);
		result.setRequiereDespacharCompleto(requiereDespacharCompleto);

		result.setTerceroIdentificacion(trim(defaultString(input.getPuntoCodigo())));
		result.setTerceroNombre(trim(defaultString(input.getPuntoNombre())));
		result.setCanalCodigoAlterno(IngredionSolicitudesDespachoConstants.CANAL_CODIGO);
		result.setCiudadCodigoAlterno(ajustarCodigoCiudad(trim(defaultString(input.getCiudadCodigoAlterno()))));
		result.setDireccion(trim(defaultString(input.getDireccion())));
		result.setPuntoCodigoAlterno(trim(defaultString(input.getPuntoCodigo())));
		result.setPuntoNombre(trim(defaultString(input.getPuntoNombre())));

		result.setContactoPrincipalNombre(trim(defaultString(input.getResponsablePrincipal())));
		result.setContactoPrincipalTelefono(trim(defaultString(input.getTelefono())));
		result.setContactoSecundarioNombre(trim(defaultString(input.getResponsableSuplente())));
		result.setContactoSecundarioTelefono(trim(defaultString(input.getTelefono())));
		result.setAutorizadoIdentificacion("");
		result.setAutorizadoNombres("");

		result.setTipoDocumentoCodigoAuxiliar1(IngredionSolicitudesDespachoConstants.TIPO_DOC_IDENTIFICADOR);
		result.setNumeroDocumentoAuxiliar1(trim(defaultString(input.getIdentificador())));

		result.setNota("");
		result.setFechaCreacionExterna(fechaCreacionExterna);
		result.setLineas(asLineas(input));

		result.setPeriodo(trim(defaultString(input.getPeriodo())));
		result.setPlantaCodigo(trim(defaultString(input.getPlantaCodigo())));
		result.setProgramaCodigo(trim(defaultString(input.getProgramaCodigo())));
		result.setProgramaNombre(trim(defaultString(input.getProgramaNombre())));
		result.setRegionalCodigo(trim(defaultString(input.getRegionalCodigo())));
		result.setRegionalNombre(trim(defaultString(input.getRegionalNombre())));
		result.setZonaCodigo(trim(defaultString(input.getZonaCodigo())));
		result.setCiudadNombre(trim(defaultString(input.getCiudadNombre())));

		return result;
	}

	private String ajustarCodigoCiudad(String codigo) {
		if (codigo != null) {
			if (codigo.length() < 5) {
				if (StringUtils.isNumeric(codigo)) {
					codigo = StringUtils.leftPad(codigo, 5, "0");
				}
			}
		}
		return codigo;
	}

	private List<IngredionSolicitudDespachoLineaDto> asLineas(SilogtranSolicitudDespachoDto input) {
		val result = new ArrayList<IngredionSolicitudDespachoLineaDto>();
		int i = 0;
		for (val e : input.getLineas()) {
			val model = asLinea(i++, input, e);
			result.add(model);
		}
		return result;
	}

	private IngredionSolicitudDespachoLineaDto asLinea(int numeroLinea, SilogtranSolicitudDespachoDto input,
			SilogtranSolicitudDespachoDto.LineaDto linea) {
		val result = new IngredionSolicitudDespachoLineaDto();

		result.setNumeroLinea(numeroLinea);
		result.setNumeroLineaExterno(String.valueOf(numeroLinea));
		result.setNumeroSubLineaExterno("");
		result.setProductoCodigoAlterno(trim(defaultString(linea.getProductoCodigo())));
		result.setProductoNombre("");
		result.setCantidad(linea.getUnidades());
		result.setBodegaCodigoAlterno(trim(defaultString(input.getBodegaCodigoAlterno())));
		result.setEstadoInventarioCodigoAlterno(IngredionSolicitudesDespachoConstants.ESTADO_INVENTARIO);
		result.setLote(linea.getLote());
		result.setPredistribucion("");
		result.setValorUnitarioDeclarado(null);

		result.setPesoInformativo(trim(defaultString(linea.getPesoInformativo())));
		result.setCantidadEmbalajeInformativo(trim(defaultString(linea.getCantidadEmbalajeInformativo())));

		return result;
	}

	@Override
	protected void validateOutput(IngredionSolicitudDespachoDto output, SilogtranSolicitudDespachoDto input,
			ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		super.validateOutput(output, input, actualizacion, errores);

		if (isEmpty(output.getPeriodo())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "PERIODO"));
		}

		if (isEmpty(input.getPlantaCodigo())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "PLANTA_CODIGO"));
		}

		if (isEmpty(input.getProgramaCodigo())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "PROGRAMA CODIGO"));
		}

		if (isEmpty(input.getProgramaNombre())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "PROGRAMA_NOMBRE"));
		}

		if (isEmpty(input.getRegionalCodigo())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "REGIONAL_CODIGO"));
		}

		if (isEmpty(input.getRegionalNombre())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "REGIONAL_NOMBRE"));
		}

		if (isEmpty(input.getZonaCodigo())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "ZONA_CODIGO"));
		}

		if (isEmpty(input.getCiudadNombre())) {
			errores.add(errorAtributoRequeridoNoSuministrado(output, "CIUDAD_NOMBRE"));
		}
	}

	@Override
	protected Object push(IngredionSolicitudDespachoDto output, SilogtranSolicitudDespachoDto input,
			ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return null;
	}

	@Override
	protected void onSuccess(Object result, IngredionSolicitudDespachoDto output, SilogtranSolicitudDespachoDto input,
			ActualizacionDto actualizacion) {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;
		val subestado = "";

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected Long updateOnSuccess(Object result, IngredionSolicitudDespachoDto output,
			SilogtranSolicitudDespachoDto input, ActualizacionDto actualizacion) {
		val model = getCrudService().create(output, actualizacion, actualizacion.getEstadoIntegracion());

		val entity = new IngredionSolicitudDespacho();
		entity.setIdSolicitudDespacho(model.getId());
		entity.setPeriodo(defaultString(input.getPeriodo()));
		entity.setPlantaCodigo(defaultString(input.getPlantaCodigo()));
		entity.setProgramaCodigo(defaultString(input.getProgramaCodigo()));
		entity.setProgramaNombre(defaultString(input.getProgramaNombre()));
		entity.setRegionalCodigo(defaultString(input.getRegionalCodigo()));
		entity.setRegionalNombre(defaultString(input.getRegionalNombre()));
		entity.setZonaCodigo(defaultString(input.getZonaCodigo()));
		entity.setCiudadNombre(defaultString(input.getCiudadNombre()));
		solicitudRepository.save(entity);

		val list = new ArrayList<IngredionSolicitudDespachoLinea>();
		for (val e : output.getLineas()) {
			val linea = new IngredionSolicitudDespachoLinea();
			linea.setIdSolicitudDespacho(model.getId());
			linea.setNumeroLinea(e.getNumeroLinea());
			linea.setPesoInformativo(e.getPesoInformativo());
			linea.setCantidadEmbalajeInformativo(e.getCantidadEmbalajeInformativo());
			list.add(linea);
		}

		lineaRepository.saveAll(list);
		return model.getId();
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estado = EstadoIntegracionType.ERROR_ESTRUCTURA;

		actualizacion.setEstadoIntegracion(estado);
	}

}
