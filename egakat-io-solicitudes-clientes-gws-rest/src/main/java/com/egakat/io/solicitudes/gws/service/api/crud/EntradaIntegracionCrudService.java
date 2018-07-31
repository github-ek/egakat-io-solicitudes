package com.egakat.io.solicitudes.gws.service.api.crud;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.services.crud.api.CrudService;
import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoEntradaIntegracionType;

@Transactional(readOnly = true)
public interface EntradaIntegracionCrudService extends CrudService<EntradaIntegracionDto, Long> {

	List<EntradaIntegracionDto> findAllByIntegracionAndEstado(String integracion,
			EstadoEntradaIntegracionType... estados);

	List<EntradaIntegracionDto> findAllByIntegracionAndEstado(String integracion,
			boolean programarNotificacion, EstadoEntradaIntegracionType... estados);

	@Transactional(readOnly = false)
	void enqueue(EntradaIntegracionDto model);

}
