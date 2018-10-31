package com.egakat.io.gws.commons.solicitudes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.io.stage.service.impl.crud.ExtendedIntegracionEntityCrudServiceImpl;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespacho;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespachoLinea;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoLineaDto;
import com.egakat.io.gws.commons.solicitudes.repository.SolicitudDespachoRepository;
import com.egakat.io.gws.commons.solicitudes.service.api.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class SolicitudDespachoCrudServiceImpl
		extends ExtendedIntegracionEntityCrudServiceImpl<SolicitudDespacho, SolicitudDespachoDto>
		implements SolicitudDespachoCrudService {

	@Autowired
	private SolicitudDespachoRepository repository;

	@Override
	protected SolicitudDespachoRepository getRepository() {
		return repository;
	}

	@Override
	protected SolicitudDespachoDto asModel(SolicitudDespacho entity) {
		val model = new SolicitudDespachoDto();

		model.setId(entity.getId());
		model.setIntegracion(entity.getIntegracion());
		model.setCorrelacion(entity.getCorrelacion());
		model.setIdExterno(entity.getIdExterno());
		model.setClienteCodigoAlterno(entity.getClienteCodigoAlterno());
		model.setServicioCodigoAlterno(entity.getServicioCodigoAlterno());
		model.setNumeroSolicitud(entity.getNumeroSolicitud());
		model.setPrefijo(entity.getPrefijo());
		model.setNumeroSolicitudSinPrefijo(entity.getNumeroSolicitudSinPrefijo());
		model.setFemi(entity.getFemi());
		model.setFema(entity.getFema());
		model.setHomi(entity.getHomi());
		model.setHoma(entity.getHoma());
		model.setRequiereTransporte(entity.isRequiereTransporte());
		model.setRequiereAgendamiento(entity.isRequiereAgendamiento());
		model.setRequiereDespacharCompleto(entity.isRequiereDespacharCompleto());
		model.setTerceroIdentificacion(entity.getTerceroIdentificacion());
		model.setTerceroNombre(entity.getTerceroNombre());
		model.setCanalCodigoAlterno(entity.getCanalCodigoAlterno());
		model.setCiudadCodigoAlterno(entity.getCiudadCodigoAlterno());
		model.setDireccion(entity.getDireccion());
		model.setPuntoCodigoAlterno(entity.getPuntoCodigoAlterno());
		model.setPuntoNombre(entity.getPuntoNombre());
		model.setAutorizadoIdentificacion(entity.getAutorizadoIdentificacion());
		model.setAutorizadoNombres(entity.getAutorizadoNombres());
		model.setNumeroOrdenCompra(entity.getNumeroOrdenCompra());
		model.setFechaOrdenCompra(entity.getFechaOrdenCompra());
		model.setNota(entity.getNota());
		model.setIdCliente(entity.getIdCliente());
		model.setIdServicio(entity.getIdServicio());
		model.setIdTercero(entity.getIdTercero());
		model.setIdCanal(entity.getIdCanal());
		model.setIdCiudad(entity.getIdCiudad());
		model.setIdPunto(entity.getIdPunto());
		model.setFechaCreacionExterna(entity.getFechaCreacionExterna());
		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());
		model.setLineas(asItemModels(entity));

		return model;
	}

	protected List<SolicitudDespachoLineaDto> asItemModels(SolicitudDespacho entity) {
		val result = new ArrayList<SolicitudDespachoLineaDto>();
		entity.getLineas().forEach(item -> {
			result.add(asItemModel(item));
		});
		return result;
	}

	protected SolicitudDespachoLineaDto asItemModel(SolicitudDespachoLinea entity) {
		val model = new SolicitudDespachoLineaDto();

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
		model.setPredistribucion(entity.getPredistribucion());
		model.setValorUnitarioDeclarado(entity.getValorUnitarioDeclarado());
		model.setIdProducto(entity.getIdProducto());
		model.setIdBodega(entity.getIdBodega());
		model.setIdEstadoInventario(entity.getIdEstadoInventario());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	@Override
	protected SolicitudDespacho mergeEntity(SolicitudDespachoDto model, SolicitudDespacho entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());
		entity.setClienteCodigoAlterno(model.getClienteCodigoAlterno());
		entity.setServicioCodigoAlterno(model.getServicioCodigoAlterno());
		entity.setNumeroSolicitud(model.getNumeroSolicitud());
		entity.setPrefijo(model.getPrefijo());
		entity.setNumeroSolicitudSinPrefijo(model.getNumeroSolicitudSinPrefijo());
		entity.setFemi(model.getFemi());
		entity.setFema(model.getFema());
		entity.setHomi(model.getHomi());
		entity.setHoma(model.getHoma());
		entity.setRequiereTransporte(model.isRequiereTransporte());
		entity.setRequiereAgendamiento(model.isRequiereAgendamiento());
		entity.setRequiereDespacharCompleto(model.isRequiereDespacharCompleto());
		entity.setTerceroIdentificacion(model.getTerceroIdentificacion());
		entity.setTerceroNombre(model.getTerceroNombre());
		entity.setCanalCodigoAlterno(model.getCanalCodigoAlterno());
		entity.setCiudadCodigoAlterno(model.getCiudadCodigoAlterno());
		entity.setDireccion(model.getDireccion());
		entity.setPuntoCodigoAlterno(model.getPuntoCodigoAlterno());
		entity.setPuntoNombre(model.getPuntoNombre());
		entity.setAutorizadoIdentificacion(model.getAutorizadoIdentificacion());
		entity.setAutorizadoNombres(model.getAutorizadoNombres());
		entity.setNumeroOrdenCompra(model.getNumeroOrdenCompra());
		entity.setFechaOrdenCompra(model.getFechaOrdenCompra());
		entity.setNota(model.getNota());

		entity.setIdCliente(model.getIdCliente());
		entity.setIdServicio(model.getIdServicio());
		entity.setIdTercero(model.getIdTercero());
		entity.setIdCanal(model.getIdCanal());
		entity.setIdCiudad(model.getIdCiudad());
		entity.setIdPunto(model.getIdPunto());
		entity.setFechaCreacionExterna(model.getFechaCreacionExterna());

		entity.setVersion(model.getVersion());

		mergeItemEntities(model, entity);

		return entity;
	}

	protected void mergeItemEntities(SolicitudDespachoDto model, SolicitudDespacho entity) {
		val inserted = model.getLineas().stream().filter(a -> a.getId() == null).collect(Collectors.toList());
		val updated = model.getLineas().stream().filter(a -> a.getId() != null).collect(Collectors.toList());
		val deleted = new ArrayList<SolicitudDespachoLinea>();

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
			val itemEntity = new SolicitudDespachoLinea();
			mergeItemEntity(itemModel, itemEntity);
			entity.add(itemEntity);
		});
	}

	protected void mergeItemEntity(SolicitudDespachoLineaDto model, SolicitudDespachoLinea entity) {

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
		entity.setVersion(model.getVersion());
	}

	@Override
	protected SolicitudDespacho newEntity() {
		return new SolicitudDespacho();
	}

	@Override
	protected SolicitudDespachoDto newModel() {
		// TODO Auto-generated method stub
		return null;
	}
}