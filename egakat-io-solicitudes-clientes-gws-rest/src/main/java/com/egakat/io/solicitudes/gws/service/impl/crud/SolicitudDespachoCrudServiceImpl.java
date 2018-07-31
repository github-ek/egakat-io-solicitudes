package com.egakat.io.solicitudes.gws.service.impl.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.solicitudes.gws.domain.SolicitudDespacho;
import com.egakat.io.solicitudes.gws.dto.SolicitudDespachoDto;
import com.egakat.io.solicitudes.gws.repository.SolicitudDespachoRepository;
import com.egakat.io.solicitudes.gws.service.api.crud.SolicitudDespachoCrudService;

import lombok.val;

@Service
public class SolicitudDespachoCrudServiceImpl extends CrudServiceImpl<SolicitudDespacho, SolicitudDespachoDto, Long>
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
				.idCorrelacion(entity.getIdCorrelacion())
				.integracion(entity.getIntegracion())
				.idExterno(entity.getIdExterno())
				.estado(entity.getEstado())
				.clienteCodigoAlterno(entity.getClienteCodigoAlterno())
				.servicioCodigoAlterno(entity.getServicioCodigoAlterno())
				.numeroSolicitud(entity.getNumeroSolicitud())
				.prefijo(entity.getPrefijo())
				.numeroSolicitudSinPrefijo(entity.getNumeroSolicitudSinPrefijo())
				.femi(entity.getFemi())
				.fema(entity.getFema())
				.homi(entity.getHomi())
				.homa(entity.getHoma())
				.requiereAgendamiento(entity.getRequiereAgendamiento())
				.requiereTransporte(entity.getRequiereTransporte())
				.requiereDespacharCompleto(entity.getRequiereDespacharCompleto())
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
	protected SolicitudDespacho asEntity(SolicitudDespachoDto model, SolicitudDespacho entity) {

		entity.setIdCorrelacion(model.getIdCorrelacion());
		entity.setIntegracion(model.getIntegracion());
		entity.setIdExterno(model.getIdExterno());
		entity.setEstado(model.getEstado());
		entity.setClienteCodigoAlterno(model.getClienteCodigoAlterno());
		entity.setServicioCodigoAlterno(model.getServicioCodigoAlterno());
		entity.setNumeroSolicitud(model.getNumeroSolicitud());
		entity.setPrefijo(model.getPrefijo());
		entity.setNumeroSolicitudSinPrefijo(model.getNumeroSolicitudSinPrefijo());
		entity.setFemi(model.getFemi());
		entity.setFema(model.getFema());
		entity.setHomi(model.getHomi());
		entity.setHoma(model.getHoma());
		entity.setRequiereAgendamiento(model.getRequiereAgendamiento());
		entity.setRequiereTransporte(model.getRequiereTransporte());
		entity.setRequiereDespacharCompleto(model.getRequiereDespacharCompleto());
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
}