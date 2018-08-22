package com.egakat.io.solicitudes.gws.service.api.crud;

import java.util.List;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;

public interface ErrorIntegracionCrudService extends CrudService<ErrorIntegracionDto, Long> {

	List<ErrorIntegracionDto> findAllByIntegracionAndIdExternoAndCorrelacion(String integracion, String idExterno,String Correlacion);

}
