package com.egakat.core.io.stage.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.io.stage.dto.ActualizacionIntegracionDto;

public interface NotificationService {

	@Transactional
	void ack(ActualizacionIntegracionDto actualizacion);

	@Transactional
	void reject(ActualizacionIntegracionDto actualizacion);

	@Transactional
	void accept(ActualizacionIntegracionDto actualizacion);
}
