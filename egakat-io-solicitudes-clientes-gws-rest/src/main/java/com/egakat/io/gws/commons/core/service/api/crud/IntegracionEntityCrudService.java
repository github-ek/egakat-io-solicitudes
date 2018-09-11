package com.egakat.io.gws.commons.core.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;

@Transactional(readOnly = true)
public interface IntegracionEntityCrudService<M extends IntegrationEntityDto> extends CrudService<M, Long> {

	boolean exists(String integracion, String idExterno);

	@Transactional
	M update(M model, List<ErrorIntegracionDto> errores);
}
