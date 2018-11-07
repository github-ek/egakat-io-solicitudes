package com.egakat.io.core.service.impl.crud;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.core.domain.IntegracionEntity;
import com.egakat.io.core.dto.IntegracionEntityDto;
import com.egakat.io.core.repository.IntegrationEntityRepository;
import com.egakat.io.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.core.service.api.crud.IntegracionEntityCrudService;

import lombok.val;

abstract public class IntegracionEntityCrudServiceImpl<E extends IntegracionEntity, M extends IntegracionEntityDto>
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