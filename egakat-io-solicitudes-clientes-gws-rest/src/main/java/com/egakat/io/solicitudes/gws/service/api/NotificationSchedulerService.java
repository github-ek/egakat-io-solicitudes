package com.egakat.io.solicitudes.gws.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;

@Transactional(readOnly = true)
public interface NotificationSchedulerService {

	@Transactional(readOnly = false)
	void ack(EntradaIntegracionDto entry);

	@Transactional(readOnly = false)
	void reject(EntradaIntegracionDto entry);

	@Transactional(readOnly = false)
	void accept(EntradaIntegracionDto entry);
}
