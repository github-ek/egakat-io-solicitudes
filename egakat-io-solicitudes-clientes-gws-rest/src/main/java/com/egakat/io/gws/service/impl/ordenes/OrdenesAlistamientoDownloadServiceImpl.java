package com.egakat.io.gws.service.impl.ordenes;

import org.springframework.stereotype.Service;

import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoDownloadService;

@Service
public class OrdenesAlistamientoDownloadServiceImpl implements OrdenesAlistamientoDownloadService{

	@Override
	public void download() {
		// TODO Auto-generated method stub
		
	}
/*
extends
		RestDownloadServiceImpl<OrdShipmentDto, OrdenAlistamientoDto> implements OrdenesAlistamientoDownloadService {

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
		return IntegracionesConstants.ORDENES_DE_ALISTAMIENTO_EN_STAGE;
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
	protected OrdShipmentDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, getResponseType(), actualizacion.getIdExterno());

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
	*/
}
