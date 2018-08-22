package com.egakat.io.solicitudes.gws.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;

@Transactional(readOnly = true)
public interface DownloadService {

	@Transactional(readOnly = false)
	void download(ActualizacionIntegracionDto entry, List<ErrorIntegracionDto> errores);

	// @Transactional(readOnly = false)
	// void acknowledge(ActualizacionIntegracionDto entry);
}
