package com.egakat.io.gws.commons.core.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;

public interface NotificationService {

	@Transactional
	void ack(ActualizacionIntegracionDto actualizacion);

	@Transactional
	void reject(ActualizacionIntegracionDto actualizacion);

	@Transactional
	void accept(ActualizacionIntegracionDto actualizacion);
}
