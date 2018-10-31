package com.egakat.core.io.stage.service.impl.crud;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.io.stage.domain.IntegrationEntity;
import com.egakat.core.io.stage.dto.IntegrationEntityDto;
import com.egakat.core.io.stage.repository.IntegrationEntityRepository;
import com.egakat.core.io.stage.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.core.io.stage.service.api.crud.IntegracionEntityCrudService;
import com.egakat.core.services.crud.impl.CrudServiceImpl;

import lombok.val;

abstract public class IntegracionEntityCrudServiceImpl<E extends IntegrationEntity, M extends IntegrationEntityDto>
		extends CrudServiceImpl<E, M, Long> implements IntegracionEntityCrudService<M> {

	@Override
	abstract protected IntegrationEntityRepository<E> getRepository();

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	protected ErrorIntegracionCrudService getErroresService() {
		return erroresService;
	}

	@Override
	public boolean exists(String integracion, String idExterno) {
		val result = getRepository().existsByIntegracionAndIdExterno(integracion, idExterno);
		return result;
	}

	@Override
	public M findOneByIntegracionAndIdExterno(String integracion, String idExterno) {
		val entities = getRepository().findAllByIntegracionAndIdExterno(integracion, idExterno);
		val entity = entities.findFirst().get();
		val result = asModel(entity);
		return result;
	}
}