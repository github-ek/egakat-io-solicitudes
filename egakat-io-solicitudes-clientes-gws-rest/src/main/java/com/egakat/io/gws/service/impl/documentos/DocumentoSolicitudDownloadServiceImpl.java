package com.egakat.io.gws.service.impl.documentos;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.io.commons.constants.IntegracionesConstants;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudDto;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudLineaDto;
import com.egakat.io.commons.documentos.service.api.DocumentoSolicitudCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.service.impl.rest.RestDownloadServiceImpl;
import com.egakat.io.gws.client.constants.GwsRestConstants;
import com.egakat.io.gws.client.dto.DocumentoDespachoClienteDto;
import com.egakat.io.gws.client.dto.DocumentoDespachoClienteLineaDto;
import com.egakat.io.gws.client.properties.GwsSolicitudesDespachoRestProperties;
import com.egakat.io.gws.service.api.documentos.DocumentoSolicitudDownloadService;

import lombok.val;

//@Service
public class DocumentoSolicitudDownloadServiceImpl
		extends RestDownloadServiceImpl<DocumentoDespachoClienteDto, DocumentoSolicitudDto>
		implements DocumentoSolicitudDownloadService {

	@Autowired
	private DocumentoSolicitudCrudService crudService;

	@Autowired
	private GwsSolicitudesDespachoRestProperties properties;

	@Autowired
	private RestClient restClient;

	@Override
	protected DocumentoSolicitudCrudService getCrudService() {
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
		return IntegracionesConstants.DOCUMENTOS_SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getApiEndPoint() {
		return GwsRestConstants.DOCUMENTOS_SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "/{id}";
	}

	protected Class<DocumentoDespachoClienteDto> getResponseType() {
		return DocumentoDespachoClienteDto.class;
	}

	@Override
	protected DocumentoDespachoClienteDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getProperties().getBasePath() + getApiEndPoint();
		val query = getQuery();

		val response = getRestClient().getOneQuery(url, query, getResponseType(), actualizacion.getIdExterno());

		val result = response.getBody();
		return result;
	}

	@Override
	protected DocumentoSolicitudDto asModel(DocumentoDespachoClienteDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {

		val prefijo = defaultString(input.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(input.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val prefijoDocumento = defaultString(input.getPrefijoDocumento());
		val numeroDocumentoSinPrefijo = String.valueOf(input.getNumeroDocumentoSinPrefijo());
		val numeroDocumento = String.format("%s-%s", prefijoDocumento, numeroDocumentoSinPrefijo);

		val model = new DocumentoSolicitudDto();

		model.setIntegracion(actualizacion.getIntegracion());
		model.setCorrelacion(actualizacion.getCorrelacion());
		model.setIdExterno(actualizacion.getIdExterno());

		model.setClienteCodigoAlterno("GWS");
		model.setTipoDocumentoCodigoAlterno("FACTURA");
		model.setNumeroSolicitud(numeroSolicitud);
		model.setPrefijo(prefijo);
		model.setNumeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo);
		model.setNumeroDocumento(numeroDocumento);
		model.setPrefijoDocumento(prefijoDocumento);
		model.setNumeroDocumentoSinPrefijo(numeroDocumentoSinPrefijo);

		model.setLineas(asLineas(input));

		return model;
	}

	protected List<DocumentoSolicitudLineaDto> asLineas(DocumentoDespachoClienteDto input) {
		val result = new ArrayList<DocumentoSolicitudLineaDto>();
		int i = 0;
		for (val e : input.getLineas()) {
			val model = asLinea(i++, e);
			result.add(model);
		}
		return result;
	}

	protected DocumentoSolicitudLineaDto asLinea(int numeroLinea, DocumentoDespachoClienteLineaDto input) {
		val model = new DocumentoSolicitudLineaDto();

		model.setNumeroLinea(numeroLinea);
		model.setNumeroLineaExterno(input.getNumeroLineaExterno());
		model.setNumeroSubLineaExterno("");
		model.setProductoCodigoAlterno(input.getProductoCodigoAlterno());
		model.setProductoNombre("");
		model.setCantidad(input.getCantidad());
		model.setBodegaCodigoAlterno("");
		model.setEstadoInventarioCodigoAlterno("");
		model.setLote("");

		return model;
	}

	protected ErrorIntegracionDto errorAtributoRequeridoNoSuministrado(ActualizacionDto entry, String attribute) {
		val format = "El atributo %s no admite valores nulos o vacios. Debe sumnistrar un valor.";
		val mensaje = String.format(format, attribute);
		val result = getErroresService().error(entry, attribute, mensaje);
		return result;
	}
}
