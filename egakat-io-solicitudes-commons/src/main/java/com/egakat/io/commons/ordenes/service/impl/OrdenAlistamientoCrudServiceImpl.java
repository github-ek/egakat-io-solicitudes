package com.egakat.io.commons.ordenes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.integration.service.impl.crud.IntegracionEntityCrudServiceImpl;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamiento;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoCancelacion;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoLinea;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoLote;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoCancelacionDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLineaDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLoteDto;
import com.egakat.io.commons.ordenes.repository.OrdenAlistamientoRepository;
import com.egakat.io.commons.ordenes.service.api.OrdenAlistamientoCrudService;

import lombok.val;

@Service
public class OrdenAlistamientoCrudServiceImpl
		extends IntegracionEntityCrudServiceImpl<OrdenAlistamiento, OrdenAlistamientoDto>
		implements OrdenAlistamientoCrudService {

	@Autowired
	private OrdenAlistamientoRepository repository;

	@Override
	protected OrdenAlistamientoRepository getRepository() {
		return repository;
	}

	@Override
	protected OrdenAlistamientoDto asModel(OrdenAlistamiento entity) {
		val model = new OrdenAlistamientoDto();

		model.setId(entity.getId());
		model.setIdOrden(entity.getIdOrden());
		model.setIntegracion(entity.getIntegracion());
		model.setIdExterno(entity.getIdExterno());
		model.setCorrelacion(entity.getCorrelacion());
		model.setClientId(entity.getClientId());
		model.setWhId(entity.getWhId());
		model.setOrdnum(entity.getOrdnum());
		model.setOrdtyp(entity.getOrdtyp());
		model.setIdCliente(entity.getIdCliente());
		model.setIdBodega(entity.getIdBodega());
		model.setIdOrden(entity.getIdOrden());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		model.setLineas(asItemModels(entity));

		return model;
	}

	protected List<OrdenAlistamientoLineaDto> asItemModels(OrdenAlistamiento entity) {
		val result = new ArrayList<OrdenAlistamientoLineaDto>();
		entity.getLineas().forEach(item -> {
			result.add(asItemModel(item));
		});
		return result;
	}

	protected OrdenAlistamientoLineaDto asItemModel(OrdenAlistamientoLinea entity) {
		val model = new OrdenAlistamientoLineaDto();

		model.setId(entity.getId());
		model.setIdOrden(entity.getOrden().getId());
		model.setOrdlin(entity.getOrdlin());
		model.setPrtnum(entity.getPrtnum());
		model.setInvsts(entity.getInvsts());
		model.setOrdqty(entity.getOrdqty());
		model.setStgqty(entity.getStgqty());
		model.setShpqty(entity.getShpqty());
		model.setNumeroLinea(entity.getNumeroLinea());
		model.setIdProducto(entity.getIdProducto());
		model.setIdEstadoInventario(entity.getIdEstadoInventario());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		model.setCancelaciones(asCancelaciones(entity));
		model.setLotes(asLotes(entity));

		return model;
	}

	private List<OrdenAlistamientoCancelacionDto> asCancelaciones(OrdenAlistamientoLinea entity) {
		val result = new ArrayList<OrdenAlistamientoCancelacionDto>();
		entity.getCancelaciones().forEach(item -> {
			result.add(asCancelacion(item));
		});
		return result;
	}

	private OrdenAlistamientoCancelacionDto asCancelacion(OrdenAlistamientoCancelacion entity) {
		val model = new OrdenAlistamientoCancelacionDto();

		model.setId(entity.getId());
		model.setIdLinea(entity.getLinea().getId());

		model.setPrtnum(entity.getPrtnum());
		model.setCancod(entity.getCancod());
		model.setLngdsc(entity.getLngdsc());
		model.setRemqty(entity.getRemqty());
		model.setCanUsrId(entity.getCanUsrId());
		model.setCanDte(entity.getCanDte());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	private List<OrdenAlistamientoLoteDto> asLotes(OrdenAlistamientoLinea entity) {
		val result = new ArrayList<OrdenAlistamientoLoteDto>();
		entity.getLotes().forEach(item -> {
			result.add(asLote(item));
		});
		return result;
	}

	private OrdenAlistamientoLoteDto asLote(OrdenAlistamientoLote entity) {
		val model = new OrdenAlistamientoLoteDto();

		model.setId(entity.getId());
		model.setIdLinea(entity.getLinea().getId());

		model.setPrtnum(entity.getPrtnum());
		model.setInvsts(entity.getInvsts());
		model.setUntqty(entity.getUntqty());
		model.setLotnum(entity.getLotnum());
		model.setOrgcod(entity.getOrgcod());
		model.setExpireDte(entity.getExpireDte());

		model.setVersion(entity.getVersion());
		model.setFechaCreacion(entity.getFechaCreacion());
		model.setFechaModificacion(entity.getFechaModificacion());

		return model;
	}

	@Override
	protected OrdenAlistamiento mergeEntity(OrdenAlistamientoDto model, OrdenAlistamiento entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());

		entity.setClientId(model.getClientId());
		entity.setWhId(model.getWhId());
		entity.setOrdnum(model.getOrdnum());
		entity.setOrdtyp(model.getOrdtyp());

		entity.setIdCliente(model.getIdCliente());
		entity.setIdBodega(model.getIdBodega());
		entity.setIdOrden(model.getIdOrden());

		entity.setVersion(model.getVersion());

		mergeItemEntities(model, entity);

		return entity;
	}

	protected void mergeItemEntities(OrdenAlistamientoDto model, OrdenAlistamiento entity) {
		val inserted = model.getLineas().stream().filter(a -> a.getId() == null).collect(Collectors.toList());
		val updated = model.getLineas().stream().filter(a -> a.getId() != null).collect(Collectors.toList());
		val deleted = new ArrayList<OrdenAlistamientoLinea>();

		entity.getLineas().stream().forEach(itemEntity -> {
			val optional = updated.stream().filter(a -> a.getId().equals(itemEntity.getId())).findFirst();

			if (optional.isPresent()) {
				mergeItemEntity(optional.get(), itemEntity);
			} else {
				deleted.add(itemEntity);
			}
		});

		deleted.stream().forEach(itemEntity -> entity.removeLinea(itemEntity));

		inserted.forEach(itemModel -> {
			val itemEntity = new OrdenAlistamientoLinea();
			mergeItemEntity(itemModel, itemEntity);
			entity.addLinea(itemEntity);
		});
	}

	protected void mergeItemEntity(OrdenAlistamientoLineaDto model, OrdenAlistamientoLinea entity) {

		entity.setOrdlin(model.getOrdlin());
		entity.setPrtnum(model.getPrtnum());
		entity.setInvsts(model.getInvsts());
		entity.setOrdqty(model.getOrdqty());
		entity.setStgqty(model.getStgqty());
		entity.setShpqty(model.getShpqty());

		entity.setNumeroLinea(model.getNumeroLinea());
		entity.setIdProducto(model.getIdProducto());
		entity.setIdEstadoInventario(model.getIdEstadoInventario());

		entity.setVersion(model.getVersion());

		mergeCancelaciones(model, entity);

		mergeLotes(model, entity);
	}

	private void mergeCancelaciones(OrdenAlistamientoLineaDto model, OrdenAlistamientoLinea entity) {
		val inserted = model.getCancelaciones().stream().filter(a -> a.getId() == null).collect(Collectors.toList());
		val updated = model.getCancelaciones().stream().filter(a -> a.getId() != null).collect(Collectors.toList());
		val deleted = new ArrayList<OrdenAlistamientoCancelacion>();

		entity.getCancelaciones().stream().forEach(itemEntity -> {
			val optional = updated.stream().filter(a -> a.getId().equals(itemEntity.getId())).findFirst();

			if (optional.isPresent()) {
				mergeCancelacion(optional.get(), itemEntity);
			} else {
				deleted.add(itemEntity);
			}
		});

		deleted.stream().forEach(itemEntity -> entity.remove(itemEntity));

		inserted.forEach(itemModel -> {
			val itemEntity = new OrdenAlistamientoCancelacion();
			mergeCancelacion(itemModel, itemEntity);
			entity.add(itemEntity);
		});
	}

	private void mergeCancelacion(OrdenAlistamientoCancelacionDto model, OrdenAlistamientoCancelacion entity) {
		entity.setPrtnum(model.getPrtnum());
		entity.setCancod(model.getCancod());
		entity.setLngdsc(model.getLngdsc());
		entity.setRemqty(model.getRemqty());
		entity.setCanUsrId(model.getCanUsrId());
		entity.setCanDte(model.getCanDte());

		entity.setVersion(model.getVersion());
	}

	private void mergeLotes(OrdenAlistamientoLineaDto model, OrdenAlistamientoLinea entity) {
		val inserted = model.getLotes().stream().filter(a -> a.getId() == null).collect(Collectors.toList());
		val updated = model.getLotes().stream().filter(a -> a.getId() != null).collect(Collectors.toList());
		val deleted = new ArrayList<OrdenAlistamientoLote>();

		entity.getLotes().stream().forEach(itemEntity -> {
			val optional = updated.stream().filter(a -> a.getId().equals(itemEntity.getId())).findFirst();

			if (optional.isPresent()) {
				mergeLote(optional.get(), itemEntity);
			} else {
				deleted.add(itemEntity);
			}
		});

		deleted.stream().forEach(itemEntity -> entity.remove(itemEntity));

		inserted.forEach(itemModel -> {
			val itemEntity = new OrdenAlistamientoLote();
			mergeLote(itemModel, itemEntity);
			entity.add(itemEntity);
		});
	}

	private void mergeLote(OrdenAlistamientoLoteDto model, OrdenAlistamientoLote entity) {
		entity.setPrtnum(model.getPrtnum());
		entity.setInvsts(model.getInvsts());
		entity.setUntqty(model.getUntqty());
		entity.setLotnum(model.getLotnum());
		entity.setOrgcod(model.getOrgcod());
		entity.setExpireDte(model.getExpireDte());

		entity.setVersion(model.getVersion());
	}

	@Override
	protected OrdenAlistamiento newEntity() {
		return new OrdenAlistamiento();
	}

	@Override
	protected OrdenAlistamientoDto newModel() {
		return new OrdenAlistamientoDto();
	}
}