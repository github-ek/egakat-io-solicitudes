package com.egakat.io.commons.ordenes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.io.commons.ordenes.domain.OrdenAlistamiento;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoCancelacion;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoLinea;
import com.egakat.io.commons.ordenes.domain.OrdenAlistamientoLote;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoCancelacionDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLineaDto;
import com.egakat.io.commons.ordenes.dto.OrdenAlistamientoLoteDto;
import com.egakat.io.commons.ordenes.repository.OrdenAlistamientoDummy;
import com.egakat.io.commons.ordenes.repository.OrdenAlistamientoRepository;
import com.egakat.io.commons.ordenes.service.api.OrdenAlistamientoCrudService;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.core.service.impl.crud.ExtendedIntegracionEntityCrudServiceImpl;
import com.egakat.io.gws.client.service.api.OrdenAlistamientoClienteLocalService;
import com.egakat.io.gws.dto.OrdenAlistamientoClienteCancelacionDto;
import com.egakat.io.gws.dto.OrdenAlistamientoClienteDto;
import com.egakat.io.gws.dto.OrdenAlistamientoClienteLineaDto;
import com.egakat.io.gws.dto.OrdenAlistamientoClienteLoteDto;

import lombok.val;

@Service
public class OrdenAlistamientoCrudServiceImpl
		extends ExtendedIntegracionEntityCrudServiceImpl<OrdenAlistamiento, OrdenAlistamientoDto>
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
		model.setIdProducto(entity.getIdProducto());
		model.setIdCausalCancelacion(entity.getIdCausalCancelacion());

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
		model.setIdProducto(entity.getIdProducto());
		model.setIdEstadoInventario(entity.getIdEstadoInventario());

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

		entity.setIdProducto(model.getIdProducto());
		entity.setIdCausalCancelacion(model.getIdCausalCancelacion());

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

		entity.setIdProducto(model.getIdProducto());
		entity.setIdEstadoInventario(model.getIdEstadoInventario());

		entity.setVersion(model.getVersion());
	}

	@Override
	protected OrdenAlistamiento newEntity() {
		return new OrdenAlistamiento();
	}

	//@Autowired
	private OrdenAlistamientoClienteLocalService ordenAlistamientoClienteService;

	@Override
	public Long upload(OrdenAlistamientoDto ord, List<ErrorIntegracionDto> errores) {
		Long result = null;
		String client_id = ord.getClientId();
		String ordnum = ord.getOrdnum();
		String wh_id = ord.getWhId();

		val orden = getRepository().findOneOrdenDeAlistamientoEnStage(client_id, ordnum, wh_id);
		val model = asOrden(ord, orden);

		try {
			ordenAlistamientoClienteService.upload(model);

			result = orden.getIdOrdenAlistamiento();
		} catch (Exception e) {
			val error = getErroresService().error(ord, "", e);
			errores.add(error);
		}

		return result;
	}

	protected OrdenAlistamientoClienteDto asOrden(OrdenAlistamientoDto ord, OrdenAlistamientoDummy orden) {
		val lineas = asLineas(orden.getId(), ord);

		// @formatter:off
		val result = OrdenAlistamientoClienteDto
				.builder()
				.idOrdenAlistamiento(orden.getIdOrdenAlistamiento())
				.id(orden.getIdExterno().toString())
				.numeroOrden(ord.getOrdnum())
				.tipoOrden(ord.getOrdtyp())
				.lineas(lineas)
				.build();
		// @formatter:on
		return result;
	}

	protected List<OrdenAlistamientoClienteLineaDto> asLineas(Long id, OrdenAlistamientoDto ord) {
		val lineas = new ArrayList<OrdenAlistamientoClienteLineaDto>();

		val alistadas = ord.getLineas();
		val solicitadas = getRepository().findOrdenesDeAlistamientoEnStageLineas(id);

		for (val s : solicitadas) {
			val cancelaciones = new ArrayList<OrdenAlistamientoClienteCancelacionDto>();
			val lotes = new ArrayList<OrdenAlistamientoClienteLoteDto>();

			val optional = alistadas.stream().filter(a -> a.getOrdlin().equals(s.getOrdlin())).findFirst();

			int cantidadDespachada = 0;
			String estadoInventario = "";
			if (optional.isPresent()) {
				val ordlin = optional.get();
				cantidadDespachada = ordlin.getShpqty() + ordlin.getStgqty();
				estadoInventario = ordlin.getInvsts();

				ordlin.getCancelaciones().forEach(a -> {
					cancelaciones.add(new OrdenAlistamientoClienteCancelacionDto(a.getCancod(), a.getRemqty()));
				});

				ordlin.getLotes().forEach(a -> {
					lotes.add(new OrdenAlistamientoClienteLoteDto(a.getLotnum(), a.getUntqty(), a.getInvsts()));
				});
			}

			int cantidadNoDespachada = s.getCantidad() - cantidadDespachada;

			// @formatter:off
			val linea = OrdenAlistamientoClienteLineaDto
					.builder()
					.numeroLineaExterno(s.getNumeroLineaExterno())
					.numeroSublineaExterno(s.getNumeroSublineaExterno())
					.productoCodigo(s.getProductoCodigoAlterno())
					.bodegaCodigoAlterno(s.getBodegaCodigoAlterno())
					.cantidadDespachada(cantidadDespachada)
					.cantidadNoDespachada(cantidadNoDespachada)
					.estadoInventario(estadoInventario)
					.bodegaCodigo(ord.getWhId())
					.cancelaciones(cancelaciones)
					.lotes(lotes)
					.build();
			// @formatter:on
			lineas.add(linea);
		}
		return lineas;
	}

	@Override
	public void updateEstadoNoficacionOrdenAlistamiento(OrdenAlistamientoDto model,
			ActualizacionDto actualizacion, EstadoIntegracionType estado, Long id) {
		update(model, actualizacion, estado);
		getRepository().updateEstadoNoficacion(id);
		getRepository().flush();
	}

	@Override
	protected OrdenAlistamientoDto newModel() {
		return new OrdenAlistamientoDto();
	}
}