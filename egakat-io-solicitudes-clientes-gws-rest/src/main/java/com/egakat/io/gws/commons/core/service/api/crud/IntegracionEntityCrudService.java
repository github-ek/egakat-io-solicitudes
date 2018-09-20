package com.egakat.io.gws.commons.core.service.api.crud;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;

public interface IntegracionEntityCrudService<M extends IntegrationEntityDto> extends CrudService<M, Long> {

	@Transactional(readOnly = true)
	boolean exists(String integracion, String idExterno);
	
	@Transactional(readOnly = true)
	M findOneByIntegracionAndIdExterno(String integracion, String idExterno);
}
