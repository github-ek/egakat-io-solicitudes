package com.egakat.io.gws.commons.solicitudes.service.impl.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespachoLinea;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.repository.SolicitudDespachoLineaRepository;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoLineaCrudService;

import lombok.val;

@Service
public class SolicitudDespachoLineaCrudServiceImpl
		extends CrudServiceImpl<SolicitudDespachoLinea, SolicitudDespachoLineaDto, Long>
		implements SolicitudDespachoLineaCrudService {

	@Autowired
	private SolicitudDespachoLineaRepository repository;

	@Override
	protected SolicitudDespachoLineaRepository getRepository() {
		return repository;
	}

	@Override
	protected SolicitudDespachoLineaDto asModel(SolicitudDespachoLinea entity) {
		// @formatter:off
		val result = SolicitudDespachoLineaDto
				.builder()
				.id(entity.getId())
				.idSolicitudDespacho(entity.getIdSolicitudDespacho())
				.numeroLinea(entity.getNumeroLinea())
				.numeroLineaExterno(entity.getNumeroLineaExterno())
				.numeroSubLineaExterno(entity.getNumeroSubLineaExterno())
				.productoCodigoAlterno(entity.getProductoCodigoAlterno())
				.productoNombre(entity.getProductoNombre())
				.cantidad(entity.getCantidad())
				.bodegaCodigoAlterno(entity.getBodegaCodigoAlterno())
				.estadoInventarioCodigoAlterno(entity.getEstadoInventarioCodigoAlterno())
				.lote(entity.getLote())
				.predistribucion(entity.getPredistribucion())
				.valorUnitarioDeclarado(entity.getValorUnitarioDeclarado())
				.idProducto(entity.getIdProducto())
				.idBodega(entity.getIdBodega())
				.idEstadoInventario(entity.getIdEstadoInventario())
				.version(entity.getVersion())
				.fechaCreacion(entity.getFechaCreacion())
				.fechaModificacion(entity.getFechaModificacion())
				.build();
		// @formatter:on
		return result;
	}

	@Override
	protected SolicitudDespachoLinea mergeEntity(SolicitudDespachoLineaDto model, SolicitudDespachoLinea entity) {

		entity.setIdSolicitudDespacho(model.getIdSolicitudDespacho());
		entity.setNumeroLinea(model.getNumeroLinea());
		entity.setNumeroLineaExterno(model.getNumeroLineaExterno());
		entity.setNumeroSubLineaExterno(model.getNumeroSubLineaExterno());
		entity.setProductoCodigoAlterno(model.getProductoCodigoAlterno());
		entity.setProductoNombre(model.getProductoNombre());
		entity.setCantidad(model.getCantidad());
		entity.setBodegaCodigoAlterno(model.getBodegaCodigoAlterno());
		entity.setEstadoInventarioCodigoAlterno(model.getEstadoInventarioCodigoAlterno());
		entity.setLote(model.getLote());
		entity.setPredistribucion(model.getPredistribucion());
		entity.setValorUnitarioDeclarado(model.getValorUnitarioDeclarado());
		entity.setIdProducto(model.getIdProducto());
		entity.setIdBodega(model.getIdBodega());
		entity.setIdEstadoInventario(model.getIdEstadoInventario());

		return entity;
	}

	@Override
	protected SolicitudDespachoLinea newEntity() {
		return new SolicitudDespachoLinea();
	}

	@Override
	public List<SolicitudDespachoLineaDto> findAllByIdSolicitudDespacho(Long id) {
		val entities = getRepository().findAllByIdSolicitudDespacho(id);
		val result = asModels(entities);
		return result;
	}
}