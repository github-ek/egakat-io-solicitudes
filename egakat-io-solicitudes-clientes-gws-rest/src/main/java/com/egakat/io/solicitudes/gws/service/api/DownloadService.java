package com.egakat.io.solicitudes.gws.service.api;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.solicitudes.gws.dto.EntradaIntegracionDto;

@Transactional(readOnly = true)
public interface DownloadService {

	@Transactional(readOnly = false)
	void download(EntradaIntegracionDto entry);

	@Transactional(readOnly = false)
	void acknowledge(EntradaIntegracionDto entry);
}
