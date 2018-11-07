package com.egakat.io.core.service.api.crud;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.core.dto.IntegracionEntityDto;

public interface IntegracionEntityCrudService<M extends IntegracionEntityDto> extends CrudService<M, Long> {

	@Transactional(readOnly = true)
	boolean exists(String integracion, String idExterno);
	
	@Transactional(readOnly = true)
	M findOneByIntegracionAndIdExterno(String integracion, String idExterno);
}
