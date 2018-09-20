package com.egakat.io.gws.cliente.service.api.deprecated;

import com.egakat.io.gws.commons.core.domain.IntegrationEntity;

public interface DataQualityService<T extends IntegrationEntity> {

	void validate();

}
