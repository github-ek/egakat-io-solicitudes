package com.egakat.io.solicitudes.gws.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;

@Transactional(readOnly = true)
public interface NotificationService {

	@Transactional(readOnly = false)
	void ack(ActualizacionIntegracionDto entry);

	@Transactional(readOnly = false)
	void reject(ActualizacionIntegracionDto entry);

	@Transactional(readOnly = false)
	void accept(ActualizacionIntegracionDto entry);
}
