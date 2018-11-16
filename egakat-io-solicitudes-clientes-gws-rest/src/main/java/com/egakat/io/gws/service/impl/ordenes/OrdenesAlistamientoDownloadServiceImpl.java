package com.egakat.io.gws.service.impl.ordenes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoCancelacionDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLineaDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLoteDto;
import com.egakat.io.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.rest.RestDownloadServiceImpl;
import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoDownloadService;
import com.egakat.wms.ordenes.client.components.WmsOrdenesRestClient;
import com.egakat.wms.ordenes.client.properties.WmsOrdenesRestProperties;
import com.egakat.wms.ordenes.constants.RestConstants;
import com.egakat.wms.ordenes.dto.alistamientos.OrdShipmentDto;
import com.egakat.wms.ordenes.dto.alistamientos.OrdShipmentLineCancelacionDto;
import com.egakat.wms.ordenes.dto.alistamientos.OrdShipmentLineDto;
import com.egakat.wms.ordenes.dto.alistamientos.OrdShipmentLineLoteDto;

import lombok.val;

@Service
public class OrdenesAlistamientoDownloadServiceImpl extends
		RestDownloadServiceImpl<OrdShipmentDto, OrdenAlistamientoDto> implements OrdenesAlistamientoDownloadService {

	@Autowired
	private WmsOrdenesRestProperties properties;

	@Autowired
	private WmsOrdenesRestClient restClient;

	@Autowired
	private OrdenAlistamientoCrudService crudService;

	@Override
	protected RestProperties getProperties() {
		return properties;
	}

	@Override
	protected RestClient getRestClient() {
		return restClient;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.suscripciones_ordenes_alistamiento;
	}

	@Override
	protected String getQuery() {
		return RestConstants.suscripciones_ordenes_alistamiento_by_pk;
	}

	@Override
	protected String getIntegracion() {
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO;
	}

	@Override
	protected OrdenAlistamientoCrudService getCrudService() {
		return crudService;
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.NO_PROCESADO, "DESCARGAR");
		return result;
	}

	@Override
	protected OrdShipmentDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, OrdShipmentDto.class, actualizacion.getArg1(),
				actualizacion.getArg0(), actualizacion.getArg2());

		val result = response.getBody();
		return result;
	}

	@Override
	protected OrdenAlistamientoDto asModel(OrdShipmentDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val model = new OrdenAlistamientoDto();

		model.setIntegracion(actualizacion.getIntegracion());
		model.setIdExterno(actualizacion.getIdExterno());
		model.setCorrelacion(actualizacion.getCorrelacion());

		model.setClientId(input.getClientId());
		model.setWhId(input.getWhId());
		model.setOrdnum(input.getOrdnum());
		model.setOrdtyp(input.getOrdtyp());

		model.setLineas(asLineas(input));
		return model;

	}

	protected List<OrdenAlistamientoLineaDto> asLineas(OrdShipmentDto input) {
		val lineas = new ArrayList<OrdenAlistamientoLineaDto>();
		int i = 0;
		for (val e : input.getLineas()) {
			val model = asLinea(i++, e);
			lineas.add(model);
		}
		return lineas;
	}

	protected OrdenAlistamientoLineaDto asLinea(int numeroLinea, OrdShipmentLineDto input) {
		val model = new OrdenAlistamientoLineaDto();

		model.setOrdlin(input.getOrdlin());
		model.setPrtnum(input.getPrtnum());
		model.setInvsts(input.getInvsts());
		model.setOrdqty(input.getOrdqty());
		model.setStgqty(input.getStgqty());
		model.setShpqty(input.getShpqty());

		model.setCancelaciones(asCancelaciones(input));

		model.setLotes(asLotes(input));

		return model;
	}

	private List<OrdenAlistamientoCancelacionDto> asCancelaciones(OrdShipmentLineDto input) {
		val result = new ArrayList<OrdenAlistamientoCancelacionDto>();
		int i = 0;
		for (val e : input.getCancelaciones()) {
			val model = asCancelacion(i++, e);
			result.add(model);
		}
		return result;
	}

	private OrdenAlistamientoCancelacionDto asCancelacion(int i, OrdShipmentLineCancelacionDto input) {
		val model = new OrdenAlistamientoCancelacionDto();

		model.setPrtnum(input.getPrtnum());
		model.setCancod(input.getCancod());
		model.setLngdsc(input.getLngdsc());
		model.setRemqty(input.getRemqty());
		model.setCanUsrId(input.getCanUsrId());
		model.setCanDte(input.getCanDte());

		return model;
	}

	private List<OrdenAlistamientoLoteDto> asLotes(OrdShipmentLineDto input) {
		val result = new ArrayList<OrdenAlistamientoLoteDto>();
		int i = 0;
		for (val e : input.getLotes()) {
			val model = asLote(i++, e);
			result.add(model);
		}
		return result;
	}

	private OrdenAlistamientoLoteDto asLote(int i, OrdShipmentLineLoteDto input) {
		val model = new OrdenAlistamientoLoteDto();

		model.setPrtnum(input.getPrtnum());
		model.setInvsts(input.getInvsts());
		model.setUntqty(input.getUntqty());
		model.setLotnum(input.getLotnum());
		model.setOrgcod(input.getOrgcod());
		model.setExpireDte(input.getExpireDte());

		return model;
	}
}
