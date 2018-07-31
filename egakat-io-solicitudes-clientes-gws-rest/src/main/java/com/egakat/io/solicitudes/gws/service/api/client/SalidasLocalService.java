package com.egakat.io.solicitudes.gws.service.api.client;

import java.util.List;

import com.egakat.core.web.client.service.api.LocalQueryService;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.gws.integraciones.solicitudes.salidas.dto.SolicitudDto;

public interface SalidasLocalService extends LocalQueryService<SolicitudDto, Integer> {

	List<Integer> getAllByStatus(String status);

	void confirmarRecibo(Integer id);

	void notificarErrores(Integer id, List<ErrorIntegracionDto> errores);
}