package com.egakat.io.commons.documentos.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.service.impl.crud.IntegracionEntityCrudServiceImpl;
import com.egakat.io.commons.documentos.domain.DocumentoSolicitud;
import com.egakat.io.commons.documentos.domain.DocumentoSolicitudLinea;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudDto;
import com.egakat.io.commons.documentos.dto.DocumentoSolicitudLineaDto;
import com.egakat.io.commons.documentos.repository.DocumentoSolicitudRepository;
import com.egakat.io.commons.documentos.service.api.DocumentoSolicitudCrudService;

import lombok.val;

@Service
public class DocumentoSolicitudCrudServiceImpl
		extends IntegracionEntityCrudServiceImpl<DocumentoSolicitud, DocumentoSolicitudDto>
		implements DocumentoSolicitudCrudService {

	@Autowired
	private DocumentoSolicitudRepository repository;

	@Override
	protected DocumentoSolicitudRepository getRepository() {
		return repository;
	}

	@Override
	protected DocumentoSolicitudDto asModel(DocumentoSolicitud entity) {
		val model = new DocumentoSolicitudDto();

		model.setId(entity.getId());
		model.setIntegracion(entity.getIntegracion());
		model.setCorrelacion(entity.getCorrelacion());
		model.setIdExterno(entity.getIdExterno());

		model.setClienteCodigoAlterno(entity.getClienteCodigoAlterno());
		model.setTipoDocumentoCodigoAlterno(entity.getTipoDocumentoCodigoAlterno());
		model.setNumeroSolicitud(entity.getNumeroSolicitud());
		model.setPrefijo(entity.getPrefijo());
		model.setNumeroSolicitudSinPrefijo(entity.getNumeroSolicitudSinPrefijo());
		model.setNumeroDocumento(entity.getNumeroDocumento());
		model.setPrefijoDocumento(entity.getPrefijoDocumento());
		model.setNumeroDocumentoSinPrefijo(entity.getNumeroDocumentoSinPrefijo());
		model.setIdCliente(entity.getIdCliente());
		model.setIdTipoDocumento(entity.getIdTipoDocumento());
		model.setIdSolicitud(entity.getIdSolicitud());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());
		model.setLineas(asItemModels(entity));

		return model;
	}

	protected List<DocumentoSolicitudLineaDto> asItemModels(DocumentoSolicitud entity) {
		val result = new ArrayList<DocumentoSolicitudLineaDto>();
		entity.getLineas().forEach(item -> {
			result.add(asItemModel(item));
		});
		return result;
	}

	protected DocumentoSolicitudLineaDto asItemModel(DocumentoSolicitudLinea entity) {
		val model = new DocumentoSolicitudLineaDto();

		model.setId(entity.getId());
		model.setNumeroLinea(entity.getNumeroLinea());
		model.setNumeroLineaExterno(entity.getNumeroLineaExterno());
		model.setNumeroSubLineaExterno(entity.getNumeroSubLineaExterno());
		model.setProductoCodigoAlterno(entity.getProductoCodigoAlterno());
		model.setProductoNombre(entity.getProductoNombre());
		model.setCantidad(entity.getCantidad());
		model.setBodegaCodigoAlterno(entity.getBodegaCodigoAlterno());
		model.setEstadoInventarioCodigoAlterno(entity.getEstadoInventarioCodigoAlterno());
		model.setLote(entity.getLote());

		model.setIdProducto(entity.getIdProducto());
		model.setIdBodega(entity.getIdBodega());
		model.setIdEstadoInventario(entity.getIdEstadoInventario());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	@Override
	protected DocumentoSolicitud mergeEntity(DocumentoSolicitudDto model, DocumentoSolicitud entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());

		entity.setClienteCodigoAlterno(model.getClienteCodigoAlterno());
		entity.setTipoDocumentoCodigoAlterno(model.getTipoDocumentoCodigoAlterno());
		entity.setNumeroSolicitud(model.getNumeroSolicitud());
		entity.setPrefijo(model.getPrefijo());
		entity.setNumeroSolicitudSinPrefijo(model.getNumeroSolicitudSinPrefijo());
		entity.setNumeroDocumento(model.getNumeroDocumento());
		entity.setPrefijoDocumento(model.getPrefijoDocumento());
		entity.setNumeroDocumentoSinPrefijo(model.getNumeroDocumentoSinPrefijo());

		entity.setIdCliente(model.getIdCliente());
		entity.setIdTipoDocumento(model.getIdTipoDocumento());
		entity.setIdSolicitud(model.getIdSolicitud());

		entity.setVersion(model.getVersion());

		mergeItemEntities(model, entity);

		return entity;
	}

	protected void mergeItemEntities(DocumentoSolicitudDto model, DocumentoSolicitud entity) {
		val inserted = model.getLineas().stream().filter(a -> a.getId() == null).collect(Collectors.toList());
		val updated = model.getLineas().stream().filter(a -> a.getId() != null).collect(Collectors.toList());
		val deleted = new ArrayList<DocumentoSolicitudLinea>();

		entity.getLineas().stream().forEach(itemEntity -> {
			val optional = updated.stream().filter(a -> a.getId().equals(itemEntity.getId())).findFirst();

			if (optional.isPresent()) {
				mergeItemEntity(optional.get(), itemEntity);
			} else {
				deleted.add(itemEntity);
			}
		});

		deleted.stream().forEach(itemEntity -> entity.remove(itemEntity));

		inserted.forEach(itemModel -> {
			val itemEntity = new DocumentoSolicitudLinea();
			mergeItemEntity(itemModel, itemEntity);
			entity.add(itemEntity);
		});
	}

	protected void mergeItemEntity(DocumentoSolicitudLineaDto model, DocumentoSolicitudLinea entity) {

		entity.setNumeroLinea(model.getNumeroLinea());
		entity.setNumeroLineaExterno(model.getNumeroLineaExterno());
		entity.setNumeroSubLineaExterno(model.getNumeroSubLineaExterno());
		entity.setProductoCodigoAlterno(model.getProductoCodigoAlterno());
		entity.setProductoNombre(model.getProductoNombre());
		entity.setCantidad(model.getCantidad());
		entity.setBodegaCodigoAlterno(model.getBodegaCodigoAlterno());
		entity.setEstadoInventarioCodigoAlterno(model.getEstadoInventarioCodigoAlterno());
		entity.setLote(model.getLote());
		entity.setIdProducto(model.getIdProducto());
		entity.setIdBodega(model.getIdBodega());
		entity.setIdEstadoInventario(model.getIdEstadoInventario());
		entity.setVersion(model.getVersion());
	}

	@Override
	protected DocumentoSolicitud newEntity() {
		return new DocumentoSolicitud();
	}

	@Override
	protected DocumentoSolicitudDto newModel() {
		return new DocumentoSolicitudDto();
	}
}