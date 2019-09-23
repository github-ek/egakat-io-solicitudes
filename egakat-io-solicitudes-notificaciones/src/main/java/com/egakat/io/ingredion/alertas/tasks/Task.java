package com.egakat.io.ingredion.alertas.tasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.core.mail.service.api.MailService;
import com.egakat.integration.service.api.crud.ErrorIntegracionCrudService;
import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;
import com.egakat.io.ingredion.alertas.service.api.ErroresConsolidadosIntegracionesAlertService;
import com.egakat.io.ingredion.alertas.service.api.ErroresIntegracionesAlertService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private ErroresIntegracionesAlertService recientesService;

	@Autowired
	private ErroresConsolidadosIntegracionesAlertService consolidadosService;

	@Autowired
	private MailService mailService;

	@Value("${cron-alertas-errores-consolidados-minus-hours}")
	private Long minusHours;

	@Scheduled(cron = "${cron-alertas-errores}")
	public void recientes() {
		try {
			//val partitions = erroresService.getActasConError();

			//partitions.forEach(actas -> {
			//	recientes(actas);
			//});
		} catch (RuntimeException e) {
			log.error("catch (" + e.getClass().getSimpleName() + " e), exception:" + e.getMessage());
		}
	}

	@Scheduled(cron = "${cron-alertas-errores-consolidados}")
	public void consolidados() {
		try {
//			val partitions = erroresService.getConsolidadoActasConError();
//
//			partitions.forEach(errores -> {
//				consolidados(erroresService);
//			});
		} catch (RuntimeException e) {
			log.error("catch (" + e.getClass().getSimpleName() + " e), exception:" + e.getMessage());
		}
	}

	protected void recientes(List<ErrorActaDto> actas) {
		try {
			log.info("Notificando actas con errores recientes. Encontrados={}", actas.size());
			if (!actas.isEmpty()) {
				val message = recientesService.getMessage(actas);
				mailService.sendMail(message);
				//erroresService.notificados(actas);
			}
		} catch (RuntimeException e) {
			log.error("catch (" + e.getClass().getSimpleName() + " e), exception:" + e.getMessage());
		}
	}

	protected void consolidados(List<ErrorActaDto> actas) {
		try {
			log.info("Notificando actas con errores (CONSOLIDADO). Encontrados={}", actas.size());

			val message = consolidadosService.getMessage(actas);
			mailService.sendMail(message);
		} catch (RuntimeException e) {
			log.error("catch (" + e.getClass().getSimpleName() + " e), exception:" + e.getMessage());
		}
	}
}