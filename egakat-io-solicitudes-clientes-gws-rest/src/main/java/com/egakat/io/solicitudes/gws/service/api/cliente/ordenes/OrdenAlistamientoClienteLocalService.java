package com.egakat.io.solicitudes.gws.service.api.cliente.ordenes;

import com.egakat.io.solicitudes.gws.dto.cliente.ordenes.OrdenAlistamientoClienteDto;

public interface OrdenAlistamientoClienteLocalService {
	Integer upload(OrdenAlistamientoClienteDto model);
}