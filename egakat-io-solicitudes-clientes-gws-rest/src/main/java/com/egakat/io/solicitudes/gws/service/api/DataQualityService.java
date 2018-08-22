package com.egakat.io.solicitudes.gws.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.egakat.core.web.client.service.api.CacheEvictSupported;
import com.egakat.io.solicitudes.gws.domain.dqs.DataQualityEntry;


@Transactional(readOnly = true)
public interface DataQualityService<T extends DataQualityEntry> extends CacheEvictSupported{

	List<String> getCorrelacionesPendientes();

	void transformar(String correlacion);
}
