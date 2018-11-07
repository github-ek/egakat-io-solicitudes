package com.egakat.io.core.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.core.dto.ActualizacionDto;

public interface NotificationService {

	@Transactional
	void ack(ActualizacionDto actualizacion);

	@Transactional
	void reject(ActualizacionDto actualizacion);

	@Transactional
	void accept(ActualizacionDto actualizacion);
}
