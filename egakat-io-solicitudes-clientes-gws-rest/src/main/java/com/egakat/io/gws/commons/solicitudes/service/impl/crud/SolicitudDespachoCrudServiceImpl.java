package com.egakat.io.gws.commons.solicitudes.service.impl.crud;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.service.impl.crud.ExtendedIntegracionEntityCrudServiceImpl;
import com.egakat.io.gws.commons.ordenes.repository.OrdenAlistamiento;
import com.egakat.io.gws.commons.solicitudes.domain.SolicitudDespacho;
import com.egakat.io.gws.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.gws.commons.solicitudes.repository.SolicitudDespachoRepository;
import com.egakat.io.gws.commons.solicitudes.service.api.crud.SolicitudDespachoCrudService;
import com.egakat.io.gws.ordenes.dto.OrdenAlistamientoClienteCancelacionDto;
import com.egakat.io.gws.ordenes.dto.OrdenAlistamientoClienteDto;
import com.egakat.io.gws.ordenes.dto.OrdenAlistamientoClienteLineaDto;
import com.egakat.io.gws.ordenes.dto.OrdenAlistamientoClienteLoteDto;
import com.egakat.io.gws.ordenes.service.api.OrdenAlistamientoClienteLocalService;
import com.egakat.wms.maestros.client.service.api.OrdStageLocalService;
import com.egakat.wms.maestros.dto.ordenes.OrdShipmentDto;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SolicitudDespachoCrudServiceImpl
		extends ExtendedIntegracionEntityCrudServiceImpl<SolicitudDespacho, SolicitudDespachoDto, String>
		implements SolicitudDespachoCrudService {

	@Autowired
	private SolicitudDespachoRepository repository;

	@Override
	protected SolicitudDespachoRepository getRepository() {
		return repository;
	}

	@Override
	protected SolicitudDespachoDto asModel(SolicitudDespacho entity) {
		// @formatter:off
		val result = SolicitudDespachoDto
				.builder()
				.id(entity.getId())
				.integracion(entity.getIntegracion())
				.correlacion(entity.getCorrelacion())
				.idExterno(entity.getIdExterno())
				.clienteCodigoAlterno(entity.getClienteCodigoAlterno())
				.servicioCodigoAlterno(entity.getServicioCodigoAlterno())
				.numeroSolicitud(entity.getNumeroSolicitud())
				.prefijo(entity.getPrefijo())
				.numeroSolicitudSinPrefijo(entity.getNumeroSolicitudSinPrefijo())
				.femi(entity.getFemi())
				.fema(entity.getFema())
				.homi(entity.getHomi())
				.homa(entity.getHoma())
				.requiereTransporte(entity.isRequiereTransporte())
				.requiereAgendamiento(entity.isRequiereAgendamiento())
				.requiereDespacharCompleto(entity.isRequiereDespacharCompleto())
				.terceroIdentificacion(entity.getTerceroIdentificacion())
				.terceroNombre(entity.getTerceroNombre())
				.canalCodigoAlterno(entity.getCanalCodigoAlterno())
				.ciudadCodigoAlterno(entity.getCiudadCodigoAlterno())
				.direccion(entity.getDireccion())
				.puntoCodigoAlterno(entity.getPuntoCodigoAlterno())
				.puntoNombre(entity.getPuntoNombre())
				.autorizadoIdentificacion(entity.getAutorizadoIdentificacion())
				.autorizadoNombres(entity.getAutorizadoNombres())
				.numeroOrdenCompra(entity.getNumeroOrdenCompra())
				.fechaOrdenCompra(entity.getFechaOrdenCompra())
				.nota(entity.getNota())
				.idCliente(entity.getIdCliente())
				.idServicio(entity.getIdServicio())
				.idTercero(entity.getIdTercero())
				.idCanal(entity.getIdCanal())
				.idCiudad(entity.getIdCiudad())
				.idPunto(entity.getIdPunto())
				.fechaCreacionExterna(entity.getFechaCreacionExterna())
				.version(entity.getVersion())
				.fechaCreacion(entity.getFechaCreacion())
				.fechaModificacion(entity.getFechaModificacion())
				.build();
		// @formatter:on
		return result;
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

		return entity;
	}

	@Override
	protected SolicitudDespacho newEntity() {
		return new SolicitudDespacho();
	}

	@Autowired
	private OrdStageLocalService ordStageService;

	@Autowired
	private OrdenAlistamientoClienteLocalService ordenAlistamientoClienteService;

	@Override
	public void upload(OrdShipmentDto ord) {
		val errores = new ArrayList<ErrorIntegracionDto>();

		String client_id = ord.getClientId();
		String ordnum = ord.getOrdnum();
		String wh_id = ord.getWhId();

		val orden = getRepository().findOneOrdenDeAlistamientoEnStage(client_id, ordnum, wh_id);
		val model = asOrden(ord, orden);

		try {
			try {
				ordenAlistamientoClienteService.upload(model);
			} catch (HttpServerErrorException e) {
				if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
					log.debug("{}", e.getResponseBodyAsString());
				}
			}

			getRepository().updateEstadoNoficacion(orden.getIdOrdenAlistamiento());
			getRepository().flush();
			ordStageService.ack(ord);
		} catch (Exception e) {
			val error = getErroresService().error(orden.getIntegracion(), orden.getIdExterno(), orden.getCorrelacion(), "", e);
			log.debug("error:{}", error);
			errores.add(error);
		}

		// actualizacionesService.updateEstadoNotificacion(entry,
		// errores,EstadoNotificacionType.NOTIFICADA, EstadoNotificacionType.ERROR);
	}

	protected OrdenAlistamientoClienteDto asOrden(OrdShipmentDto ord, OrdenAlistamiento orden) {
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

	protected List<OrdenAlistamientoClienteLineaDto> asLineas(Long id, OrdShipmentDto ord) {
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
	public List<SolicitudDespachoDto> findTopByEstado(String estado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Slice<SolicitudDespachoDto> findByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
}