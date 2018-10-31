package com.egakat.io.gws.cliente.service.impl.ordenes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;
import com.egakat.core.io.stage.dto.ErrorIntegracionDto;
import com.egakat.core.io.stage.service.impl.RestDownloadServiceImpl;
import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.gws.cliente.service.api.ordenes.OrdenesAlistamientoDownloadService;
import com.egakat.io.gws.commons.configuration.constants.IntegracionesConstants;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoCancelacionDto;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoLineaDto;
import com.egakat.io.gws.commons.ordenes.dto.OrdenAlistamientoLoteDto;
import com.egakat.io.gws.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.wms.maestros.constants.RestConstants;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentDto;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentLineCancelacionDto;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentLineDto;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentLineLoteDto;
import com.egakat.wms.maestros.properties.WmsRestProperties;

import lombok.val;

@Service
public class OrdenesAlistamientoDownloadServiceImpl extends RestDownloadServiceImpl<OrdShipmentDto, OrdenAlistamientoDto>
		implements OrdenesAlistamientoDownloadService {

	@Autowired
	private OrdenAlistamientoCrudService crudService;

	@Autowired
	private WmsRestProperties properties;

	@Autowired
	private RestClient restClient;

	@Override
	protected OrdenAlistamientoCrudService getCrudService() {
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
		return IntegracionesConstants.ORDENES_DE_SALIDA_EN_STAGE;
	}

	@Override
	protected String getApiEndPoint() {
		return RestConstants.ordenes_alistamiento + RestConstants.ordenes_alistamiento_suscripciones;
	}

	@Override
	protected String getQuery() {
		return "/{id}";
	}

	protected Class<OrdShipmentDto> getResponseType() {
		return OrdShipmentDto.class;
	}

	@Override
	protected OrdShipmentDto getInput(ActualizacionIntegracionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, getResponseType(), actualizacion.getIdExterno());

		val result = response.getBody();
		return result;
	}

	@Override
	protected OrdenAlistamientoDto asModel(OrdShipmentDto input, ActualizacionIntegracionDto actualizacion) {
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
