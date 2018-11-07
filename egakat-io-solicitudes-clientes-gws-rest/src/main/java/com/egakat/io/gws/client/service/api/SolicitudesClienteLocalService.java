package com.egakat.io.gws.client.service.api;

import java.util.List;

import com.egakat.core.web.client.service.api.LocalQueryService;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.dto.SolicitudDespachoClienteDto;

public interface SolicitudesClienteLocalService extends LocalQueryService<SolicitudDespachoClienteDto, Integer> {

	List<Integer> getAllByStatus(String status);

	void confirmarRecibo(Integer id);
	
	void aceptar(Integer id);

	void rechazar(Integer id, List<ErrorIntegracionDto> errores);

	void confirmarReciboDocumento(Integer id);
}