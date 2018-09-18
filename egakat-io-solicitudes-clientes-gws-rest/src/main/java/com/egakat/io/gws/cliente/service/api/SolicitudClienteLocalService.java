package com.egakat.io.gws.cliente.service.api;

import java.util.List;

import com.egakat.core.web.client.service.api.LocalQueryService;
import com.egakat.io.gws.cliente.dto.SolicitudClienteDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;

public interface SolicitudClienteLocalService extends LocalQueryService<SolicitudClienteDto, Integer> {

	List<Integer> getAllByStatus(String status);

	void confirmarRecibo(Integer id);
	
	void aceptar(Integer id);

	void rechazar(Integer id, List<ErrorIntegracionDto> errores);
}