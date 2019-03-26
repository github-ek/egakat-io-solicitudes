package com.egakat.io.clientes.gws.service.impl.solicitudes;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.mapas.dto.MapaDto;
import com.egakat.integration.mapas.service.api.MapaCrudService;
import com.egakat.integration.service.impl.MapServiceImpl;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.MapasConstants;
import com.egakat.io.clientes.gws.constants.SolicitudDespachoClienteEstadoConstants;
import com.egakat.io.clientes.gws.service.api.solicitudes.SolicitudesDespachoMapService;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class SolicitudesDespachoMapServiceImpl extends MapServiceImpl<SolicitudDespachoDto>
		implements SolicitudesDespachoMapService {

	@Autowired
	private SolicitudDespachoCrudService crudService;

	@Autowired
	private LookUpService lookUpLocalService;

	@Autowired
	private MapaCrudService mapaService;

	@Override
	protected SolicitudDespachoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.SOLICITUDES_DESPACHO;
	}

	protected LookUpService getLookUpService() {
		return lookUpLocalService;
	}

	protected MapaCrudService getMapaService() {
		return mapaService;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;

		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), estado, "", SolicitudDespachoClienteEstadoConstants.RECIBIDA_OPL);
		return result;
	}

	@Override
	protected void map(SolicitudDespachoDto input, ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		translateCliente(input);
		translateServicio(input);
		translateTercero(input);
		translateCanal(input);
		translateCiudad(input);
		translatePunto(input);

		input.getLineas().parallelStream().forEach(linea -> {
			translateProducto(input, linea);
			translateBodega(input, linea);
			translateEstadoInventario(input, linea);
		});
	}

	@Override
	protected MapaDto getMapa(Long id) {
		return getMapaService().findOneById(id);
	}

	private void translateCliente(SolicitudDespachoDto model) {
		String key = defaultKey(model.getClienteCodigoAlterno());

		model.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(key);
		model.setIdCliente(id);
	}

	private void translateServicio(SolicitudDespachoDto model) {
		String key = defaultKey(model.getServicioCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_SERVICIO_CODIGO_ALTERNO, key);

		model.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(key);
		model.setIdServicio(id);
	}

	private void translateTercero(SolicitudDespachoDto model) {
		model.setIdTercero(null);

		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(model.getTerceroIdentificacion());

			val id = getLookUpService().findTerceroIdByIdAndNumeroIdentificacion(cliente.longValue(), key);
			model.setIdTercero(id);
		}
	}

	private void translateCanal(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCanalCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_CANAL_CODIGO_ALTERNO, key);

		model.setIdCanal(null);
		val id = getLookUpService().findCanalIdByCodigo(key);
		model.setIdCanal(id);
	}

	private void translateCiudad(SolicitudDespachoDto model) {
		String key = defaultKey(model.getCiudadCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_CIUDAD_CODIGO_ALTERNO, key);

		model.setIdCiudad(null);
		Long id = null;
		if (!StringUtils.isNumeric(key)) {
			id = getLookUpService().findCiudadIdByNombreAlterno(key);
		}

		if (id == null) {
			id = getLookUpService().findCiudadIdByCodigo(key);
		}
		model.setIdCiudad(id);
	}

	private void translatePunto(SolicitudDespachoDto model) {
		model.setIdPunto(null);
		val tercero = model.getIdTercero();
		if (tercero != null) {
			String key = defaultKey(model.getPuntoCodigoAlterno());

			val id = getLookUpService().findPuntoIdByTerceroIdAndPuntoCodigo(tercero.longValue(), key);
			model.setIdPunto(id);
		}
	}

	private void translateProducto(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		linea.setIdProducto(null);
		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(linea.getProductoCodigoAlterno());

			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), key);
			linea.setIdProducto(id);
		}
	}

	private void translateBodega(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getBodegaCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_BODEGA_CODIGO_ALTERNO, key);

		linea.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		linea.setIdBodega(id);
	}

	private void translateEstadoInventario(SolicitudDespachoDto model, SolicitudDespachoLineaDto linea) {
		String key = defaultKey(linea.getEstadoInventarioCodigoAlterno());
		key = getValueFromMapOrDefault(MapasConstants.MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, key);

		linea.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(key);
		linea.setIdEstadoInventario(id);
	}

	@Override
	protected void validate(SolicitudDespachoDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		errores.clear();

		List<SolicitudDespachoLineaDto> lineas = input.getLineas();
		if (input.getIdCliente() == null) {
			errores.add(errorAtributoNoHomologado(input, "CLIENTE", input.getClienteCodigoAlterno()));
		}

		if (input.getIdServicio() == null) {
			errores.add(errorAtributoMapeableNoHomologado(input, "SERVICIO", input.getServicioCodigoAlterno(),
					MapasConstants.MAPA_SERVICIO_CODIGO_ALTERNO));
		}

		if (StringUtils.isEmpty(input.getNumeroSolicitud())) {
			errores.add(errorAtributoRequeridoNoSuministrado(input, "NUMERO_SOLICITUD"));
		}
		// TODO VALIDAR numero solicitud no existente

		if (input.getIdCanal() == null) {
			errores.add(errorAtributoMapeableNoHomologado(input, "CANAL", input.getCanalCodigoAlterno(),
					MapasConstants.MAPA_CANAL_CODIGO_ALTERNO));
		}

		if (input.isRequiereTransporte()) {
			if (input.getIdCiudad() == null) {
				errores.add(errorAtributoNoHomologado(input, "CIUDAD", input.getCiudadCodigoAlterno()));
			}

			if (StringUtils.isEmpty(input.getDireccion())) {
				errores.add(errorAtributoRequeridoNoSuministrado(input, "DIRECCION"));
			}
		}

		// TODO VALIDAR ID punto

		lineas.parallelStream().forEach(linea -> {
			// TODO VALIDAR LINEAS Y SUBLINEAS EXTERNAS UNICAS
			if (linea.getIdProducto() == null) {
				errores.add(
						errorAtributoNoHomologado(input, "PRODUCTO", linea.getProductoCodigoAlterno(), asArg(linea)));
			}
			if (linea.getIdBodega() == null) {
				val error = errorAtributoMapeableNoHomologado(input, "BODEGA", linea.getBodegaCodigoAlterno(),
						MapasConstants.MAPA_BODEGA_CODIGO_ALTERNO, asArg(linea));
				error.setArg4(linea.getBodegaCodigoAlterno());
				errores.add(error);
			}
			if (linea.getIdEstadoInventario() == null) {
				errores.add(errorAtributoMapeableNoHomologado(input, "ESTADO_INVENTARIO",
						linea.getEstadoInventarioCodigoAlterno(),
						MapasConstants.MAPA_ESTADO_INVENTARIO_CODIGO_ALTERNO, asArg(linea)));
			}
		});

		val bodegas = lineas.stream().filter(a -> a.getIdBodega() != null).map(a -> a.getIdBodega()).distinct()
				.collect(Collectors.toList());

		if (bodegas.size() > 1) {
			errores.add(errorMultiplesBodegasDetectadas(input, "BODEGA"));
		}
	}

	private String[] asArg(SolicitudDespachoLineaDto linea, String... args) {
		val result = new String[6 + args.length];
		int i = 0;
		result[i++] = String.valueOf(linea.getNumeroLinea());
		result[i++] = linea.getNumeroLineaExterno();
		result[i++] = linea.getNumeroSubLineaExterno();
		result[i++] = linea.getProductoCodigoAlterno();
		result[i++] = linea.getProductoNombre();
		result[i++] = String.valueOf(linea.getCantidad());

		for (val a : args) {
			result[i++] = a;
		}

		return result;
	}

	private ErrorIntegracionDto errorMultiplesBodegasDetectadas(SolicitudDespachoDto entry, String codigo,
			String... arg) {
		val mensaje = "Se detecto que esta solicitud esta asociada a mas de una bodega.";
		val result = getErroresService().error(entry, codigo, mensaje, arg);
		return result;
	}

	@Override
	protected void onSuccess(SolicitudDespachoDto input, ActualizacionDto actualizacion) {
		EstadoIntegracionType estado = EstadoIntegracionType.VALIDADO;
		String subestado = "";

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(SolicitudDespachoDto input, ActualizacionDto actualizacion) {
		getCrudService().update(input, actualizacion, actualizacion.getEstadoIntegracion());
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estado = EstadoIntegracionType.ERROR_VALIDACION;

		actualizacion.setEstadoIntegracion(estado);
	}
}
