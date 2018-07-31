package com.egakat.io.solicitudes.gws.service.api;

import com.egakat.io.solicitudes.gws.service.api.crud.EntradaIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

public interface IntegrationService {

	String getCodigoIntegracion();

	TakeFeedsService getTakeFeedsService();

	DownloadService getDownloadService();

	NotificationSchedulerService getNotificationSchedulerService();

	EntradaIntegracionCrudService getEntradasService();

	ErrorIntegracionCrudService getErroresService();

	void execute();

}
