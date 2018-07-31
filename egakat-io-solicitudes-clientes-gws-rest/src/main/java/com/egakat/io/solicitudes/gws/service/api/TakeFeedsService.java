package com.egakat.io.solicitudes.gws.service.api;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TakeFeedsService {

	String getCodigoIntegracion();

	@Transactional(readOnly = false)
	void takeFeeds();
}
