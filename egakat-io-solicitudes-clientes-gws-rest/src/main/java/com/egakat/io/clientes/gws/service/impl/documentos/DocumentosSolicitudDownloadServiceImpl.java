package com.egakat.io.clientes.gws.service.impl.documentos;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.web.client.components.RestClient;
import com.egakat.core.web.client.properties.RestProperties;
import com.egakat.integration.dto.ActualizacionDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.integration.enums.EstadoIntegracionType;
import com.egakat.integration.service.impl.rest.RestIntegracionEntityDownloadServiceImpl;
import com.egakat.io.clientes.gws.components.GwsRestClient;
import com.egakat.io.clientes.gws.constants.IntegracionesConstants;
import com.egakat.io.clientes.gws.constants.RestConstants;
import com.egakat.io.clientes.gws.dto.DocumentoDespachoClienteDto;
import com.egakat.io.clientes.gws.dto.DocumentoDespachoClienteLineaDto;
import com.egakat.io.clientes.gws.properties.SolicitudesDespachoRestProperties;
import com.egakat.io.clientes.gws.service.api.documentos.DocumentosSolicitudDownloadService;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudDto;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudLineaDto;
import com.egakat.io.commons.documentos.service.api.DocumentoSolicitudCrudService;

import lombok.val;

@Service
public class DocumentosSolicitudDownloadServiceImpl
		extends RestIntegracionEntityDownloadServiceImpl<DocumentoDespachoClienteDto, DocumentoSolicitudDto, Object>
		implements DocumentosSolicitudDownloadService {

	@Autowired
	private DocumentoSolicitudCrudService crudService;

	@Autowired
	private SolicitudesDespachoRestProperties properties;

	@Autowired
	private GwsRestClient restClient;

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
		return RestConstants.DOCUMENTOS_SOLICITUDES_DESPACHO;
	}

	@Override
	protected String getQuery() {
		return "/{id}";
	}

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val estado = EstadoIntegracionType.NO_PROCESADO;
		val subestado = "";

		val result = getActualizacionesService()
				.findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(getIntegracion(), estado, subestado);
		return result;
	}

	@Override
	protected DocumentoDespachoClienteDto getInput(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = getQuery();
		val id = actualizacion.getIdExterno();

		val response = getRestClient().getOneQuery(url, query, DocumentoDespachoClienteDto.class, id);
		val result = response.getBody();
		return result;
	}

	@Override
	protected DocumentoSolicitudDto asOutput(DocumentoDespachoClienteDto input, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {

		val prefijo = defaultString(input.getPrefijo());
		val numeroSolicitudSinPrefijo = String.valueOf(input.getNumeroSolicitudSinPrefijo());
		val numeroSolicitud = String.format("%s-%s", prefijo, numeroSolicitudSinPrefijo);

		val prefijoDocumento = defaultString(input.getPrefijoDocumento());
		val numeroDocumentoSinPrefijo = String.valueOf(input.getNumeroDocumentoSinPrefijo());
		val numeroDocumento = String.format("%s-%s", prefijoDocumento, numeroDocumentoSinPrefijo);

		val result = new DocumentoSolicitudDto();

		result.setIntegracion(actualizacion.getIntegracion());
		result.setCorrelacion(actualizacion.getCorrelacion());
		result.setIdExterno(actualizacion.getIdExterno());

		result.setClienteCodigoAlterno("GWS");
		result.setTipoDocumentoCodigoAlterno("FACTURA");
		result.setNumeroSolicitud(numeroSolicitud);
		result.setPrefijo(prefijo);
		result.setNumeroSolicitudSinPrefijo(numeroSolicitudSinPrefijo);
		result.setNumeroDocumento(numeroDocumento);
		result.setPrefijoDocumento(prefijoDocumento);
		result.setNumeroDocumentoSinPrefijo(numeroDocumentoSinPrefijo);

		result.setLineas(asLineas(input));

		return result;
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
		val result = new DocumentoSolicitudLineaDto();

		result.setNumeroLinea(numeroLinea);
		result.setNumeroLineaExterno(input.getNumeroLineaExterno());
		result.setNumeroSubLineaExterno("");
		result.setProductoCodigoAlterno(input.getProductoCodigoAlterno());
		result.setProductoNombre("");
		result.setCantidad(input.getCantidad());
		result.setBodegaCodigoAlterno("");
		result.setEstadoInventarioCodigoAlterno("");
		result.setLote("");

		return result;
	}

	@Override
	protected Object push(DocumentoSolicitudDto output, DocumentoDespachoClienteDto input,
			ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		return null;
	}

	@Override
	protected void onSuccess(Object result, DocumentoSolicitudDto output, DocumentoDespachoClienteDto input,
			ActualizacionDto actualizacion) {
		val estado = EstadoIntegracionType.ESTRUCTURA_VALIDA;
		val subestado = "";

		actualizacion.setEstadoIntegracion(estado);
		actualizacion.setSubEstadoIntegracion(subestado);
		actualizacion.setReintentos(0);
	}

	@Override
	protected void updateOnSuccess(Object result, DocumentoSolicitudDto output, DocumentoDespachoClienteDto input,
			ActualizacionDto actualizacion) {
		getCrudService().create(output, actualizacion, actualizacion.getEstadoIntegracion());
	}

	@Override
	protected void onError(ActualizacionDto actualizacion, List<ErrorIntegracionDto> errores) {
		val estado = EstadoIntegracionType.ERROR_ESTRUCTURA;

		actualizacion.setEstadoIntegracion(estado);
	}
}
