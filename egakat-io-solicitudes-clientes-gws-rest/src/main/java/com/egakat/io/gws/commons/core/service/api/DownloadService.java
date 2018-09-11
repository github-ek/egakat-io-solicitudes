package com.egakat.io.gws.commons.core.service.api;

import java.util.List;

import com.egakat.io.gws.commons.core.dto.ActualizacionIntegracionDto;
import com.egakat.io.gws.commons.core.dto.ErrorIntegracionDto;

public interface DownloadService {
	
	void download();

	void download(ActualizacionIntegracionDto entry, List<ErrorIntegracionDto> errores);
}
