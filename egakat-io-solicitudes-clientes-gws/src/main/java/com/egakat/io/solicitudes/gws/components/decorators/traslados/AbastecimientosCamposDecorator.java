package com.egakat.io.solicitudes.gws.components.decorators.traslados;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.files.dto.EtlRequestDto;
import com.egakat.io.solicitudes.domain.Solicitud;
import com.egakat.io.solicitudes.domain.salidas.Traslado;

import lombok.val;

public class AbastecimientosCamposDecorator extends Decorator<Traslado, Long> {

	public AbastecimientosCamposDecorator(Decorator<Traslado, Long> inner) {
		super(inner);
	}

	@Override
	public EtlRequestDto<Traslado, Long> transformar(EtlRequestDto<Traslado, Long> archivo) {
		val result = super.transformar(archivo);

		val registros = result.getRegistros();
		val now = LocalDate.now();
		val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		val femi = add(now, 2).format(formatter);
		val fema = add(now, 5).format(formatter);

		for (val registro : registros) {
			registro.getDatos().put(Solicitud.FEMI, femi);
			registro.getDatos().put(Solicitud.FEMA, fema);
		}

		return result;
	}

	public LocalDate add(LocalDate date, int workdays) {
		if (workdays < 1) {
			return date;
		}

		LocalDate result = date;
		int addedDays = 0;
		while (addedDays < workdays) {
			result = result.plusDays(1);
			if (!(result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
				++addedDays;
			}
		}

		return result;
	}

}