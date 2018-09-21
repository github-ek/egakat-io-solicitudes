package com.egakat.io.gws.cliente.service.api.solicitudes;

import java.util.List;

import com.egakat.core.web.client.service.api.LocalQueryService;
import com.egakat.io.gws.cliente.dto.SolicitudDespachoClienteDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;

public interface SolicitudesClienteLocalService extends LocalQueryService<SolicitudDespachoClienteDto, Integer> {

	List<Integer> getAllByStatus(String status);

	void confirmarRecibo(Integer id);
	
	void aceptar(Integer id);

	void rechazar(Integer id, List<ErrorIntegracionDto> errores);
}