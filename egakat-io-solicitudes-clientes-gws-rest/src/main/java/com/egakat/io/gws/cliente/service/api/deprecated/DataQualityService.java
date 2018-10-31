package com.egakat.io.gws.cliente.service.api.deprecated;

import com.egakat.core.io.stage.domain.IntegrationEntity;

public interface DataQualityService<T extends IntegrationEntity> {

	void validate();

}
