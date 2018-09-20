package com.egakat.io.gws.commons.core.service.impl.crud;

import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.gws.commons.core.domain.IntegrationEntity;
import com.egakat.io.gws.commons.core.dto.IntegrationEntityDto;
import com.egakat.io.gws.commons.core.repository.IntegrationEntityRepository;
import com.egakat.io.gws.commons.core.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.gws.commons.core.service.api.crud.IntegracionEntityCrudService;

import lombok.val;

abstract public class IntegrationEntityCrudServiceImpl<E extends IntegrationEntity, M extends IntegrationEntityDto>
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