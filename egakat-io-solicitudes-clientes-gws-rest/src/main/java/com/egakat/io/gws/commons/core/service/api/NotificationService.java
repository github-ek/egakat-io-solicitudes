package com.egakat.io.gws.commons.core.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;

public interface NotificationService {

	@Transactional(readOnly = false)
	void ack(ActualizacionIntegracionDto entry);

	@Transactional(readOnly = false)
	void reject(ActualizacionIntegracionDto entry);

	@Transactional(readOnly = false)
	void accept(ActualizacionIntegracionDto entry);
}
