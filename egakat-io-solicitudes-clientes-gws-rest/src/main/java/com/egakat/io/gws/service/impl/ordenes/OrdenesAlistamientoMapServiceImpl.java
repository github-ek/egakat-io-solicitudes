package com.egakat.io.gws.service.impl.ordenes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLineaDto;
import com.egakat.io.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.MapServiceImpl;

import lombok.val;

@Service
public class OrdenesAlistamientoMapServiceImpl extends MapServiceImpl<OrdenAlistamientoDto> {

	@Autowired
	private OrdenAlistamientoCrudService crudService;

	@Autowired
	private LookUpService lookUpLocalService;

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO;
	}

	@Override
	protected OrdenAlistamientoCrudService getCrudService() {
		return crudService;
	}

	protected LookUpService getLookUpService() {
		return lookUpLocalService;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.ESTRUCTURA_VALIDA, "");
		return result;
	}

	@Override
	protected void map(OrdenAlistamientoDto model, List<ErrorIntegracionDto> errores) {
		translateCliente(model);
		translateBodega(model);
		translateOrdenAlistamiento(model);

		model.getLineas().parallelStream().forEach(linea -> {
			translateProducto(model, linea);
			translateEstadoInventario(model, linea);
			translateNumeroLinea(model, linea);
		});
	}

	private void translateCliente(OrdenAlistamientoDto model) {
		String key = defaultKey(model.getClientId());

		model.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigoWms(key);
		model.setIdCliente(id);
	}

	private void translateBodega(OrdenAlistamientoDto model) {
		String key = defaultKey(model.getWhId());

		model.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(key);
		model.setIdBodega(id);
	}

	private void translateOrdenAlistamiento(OrdenAlistamientoDto model) {
		model.setIdOrden(null);
		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(model.getOrdnum());

			val id = 0L;// getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(),
						// key);
			model.setIdOrden(id);
		}
	}

	private void translateProducto(OrdenAlistamientoDto model, OrdenAlistamientoLineaDto linea) {
		linea.setIdProducto(null);
		val cliente = model.getIdCliente();
		if (cliente != null) {
			String key = defaultKey(linea.getPrtnum());

			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), key);
			linea.setIdProducto(id);
		}
	}

	private void translateEstadoInventario(OrdenAlistamientoDto model, OrdenAlistamientoLineaDto linea) {
		String key = defaultKey(linea.getInvsts());

		linea.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(key);
		linea.setIdEstadoInventario(id);
	}

	private void translateNumeroLinea(OrdenAlistamientoDto model, OrdenAlistamientoLineaDto linea) {
		linea.setNumeroLinea(null);
		try {
			val numeroLinea = Integer.parseInt(linea.getOrdlin());
			linea.setNumeroLinea(numeroLinea);
		} catch (NumberFormatException e) {
			
		}
	}

	@Override
	protected void validate(OrdenAlistamientoDto model, List<ErrorIntegracionDto> errores) {
		errores.clear();

		List<OrdenAlistamientoLineaDto> lineas = model.getLineas();
		if (model.getIdCliente() == null) {
			errores.add(errorAtributoNoHomologado(model, "CLIENT_ID", model.getClientId()));
		}

		if (model.getIdBodega() == null) {
			errores.add(errorAtributoNoHomologado(model, "WH_ID", model.getWhId()));
		}

		if (model.getIdOrden() == null) {
			errores.add(errorAtributoNoHomologado(model, "ORDNUM", model.getWhId()));
		}

		lineas.parallelStream().forEach(linea -> {
			// TODO VALIDAR LINEAS Y SUBLINEAS EXTERNAS UNICAS
			if (linea.getIdProducto() == null) {
				errores.add(
						errorAtributoNoHomologado(model, "PRTNUM", linea.getPrtnum(), asArg(linea)));
			}
			if (linea.getIdEstadoInventario() == null) {
				errores.add(
						errorAtributoNoHomologado(model, "INVSTS", linea.getInvsts(), asArg(linea)));
			}
		});
	}

	private String[] asArg(OrdenAlistamientoLineaDto linea, String... args) {
		val result = new String[6 + args.length];
		int i = 0;
		result[i++] = linea.getOrdlin();
		result[i++] = linea.getPrtnum();
		result[i++] = linea.getInvsts();
		result[i++] = String.valueOf(linea.getStgqty());

		for (val a : args) {
			result[i++] = a;
		}

		return result;
	}
}
