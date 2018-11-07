package com.egakat.io.gws.service.impl.ordenes;

import org.springframework.stereotype.Service;

import com.egakat.io.gws.service.api.ordenes.OrdenesAlistamientoPullService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenesAlistamientoPullServiceImpl implements OrdenesAlistamientoPullService {@Override
	public void pull() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * @Autowired private WmsRestProperties properties;
	 * 
	 * @Autowired private RestClient restClient;
	 * 
	 * 
	 * @Override protected RestProperties getProperties() { return properties; }
	 * 
	 * @Override protected RestClient getRestClient() { return restClient; }
	 * 
	 * @Override protected String getIntegracion() { return
	 * IntegracionesConstants.ORDENES_DE_ALISTAMIENTO_EN_STAGE; }
	 * 
	 * @Override protected String getApiEndPoint() { return
	 * RestConstants.ordenes_alistamiento +
	 * RestConstants.ordenes_alistamiento_suscripciones; }
	 * 
	 * @Override protected String getQuery() { return ""; }
	 * 
	 * @Override public void pull() { val correlacion = defaultCorrelacion();
	 * 
	 * try { val inputs = pullSuscripciones();
	 * 
	 * enqueue(correlacion, inputs); } catch (RuntimeException e) {
	 * getErroresService().create(getIntegracion(), correlacion, "", e);
	 * log.error("Exception:", e); } }
	 * 
	 * protected List<Integer> pullSuscripciones(Object... arg) { val url =
	 * getProperties().getBasePath() + getApiEndPoint(); val query = getQuery();
	 * 
	 * val response = getRestClient().getAllQuery(url, query, Integer[].class, arg);
	 * val result = Arrays.asList(response.getBody()); return result; }
	 * 
	 * @Override protected ActualizacionDto asModel(String correlacion, Integer
	 * input) { val result = new ActualizacionDto();
	 * 
	 * result.setIntegracion(getIntegracion());
	 * result.setIdExterno(input.toString()); result.setCorrelacion(correlacion);
	 * result.setEstadoIntegracion(EstadoIntegracionType.NO_PROCESADO);
	 * result.setEstadoNotificacion(EstadoNotificacionType.SIN_NOVEDAD);
	 * 
	 * return result; }
	 * 
	 */
}
