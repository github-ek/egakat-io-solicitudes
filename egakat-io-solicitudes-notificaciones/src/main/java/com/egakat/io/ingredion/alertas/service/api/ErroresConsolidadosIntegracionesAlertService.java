package com.egakat.io.ingredion.alertas.service.api;

import java.util.List;

import com.egakat.core.alertas.service.api.AlertService;
import com.egakat.core.mail.dto.MailMessageDto;
import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;


public interface ErroresConsolidadosIntegracionesAlertService extends AlertService<List<ErrorActaDto>, MailMessageDto> {

}
