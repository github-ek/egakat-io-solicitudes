package com.egakat.core.io.stage.service.api.crud;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.services.crud.api.CrudService;

public interface IntegracionEntityCrudService<M extends IntegrationEntityDto> extends CrudService<M, Long> {

	@Transactional(readOnly = true)
	boolean exists(String integracion, String idExterno);
	
	@Transactional(readOnly = true)
	M findOneByIntegracionAndIdExterno(String integracion, String idExterno);
}
