package com.egakat.io.solicitudes.gws.service.api.cliente.solicitudes;

import java.util.List;

import com.egakat.core.web.client.service.api.LocalQueryService;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.cliente.solicitudes.SolicitudClienteDto;

public interface SolicitudClienteLocalService extends LocalQueryService<SolicitudClienteDto, Integer> {

	List<Integer> getAllByStatus(String status);

	void confirmarRecibo(Integer id);
	
	void aceptar(Integer id);

	void rechazar(Integer id, List<ErrorIntegracionDto> errores);
}