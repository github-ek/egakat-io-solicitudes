package com.egakat.io.solicitudes.heinz.components.decorators.salidas;

import com.egakat.integration.core.files.components.decorators.Decorator;
import com.egakat.integration.files.dto.EtlRequestDto;
import com.egakat.integration.files.dto.RegistroDto;
import com.egakat.io.solicitudes.domain.salidas.Salida;

import lombok.val;

public class SalidasCamposDecorator extends Decorator<Salida, Long> {

	public SalidasCamposDecorator(Decorator<Salida, Long> inner) {
		super(inner);
	}

	@Override
	public EtlRequestDto<Salida, Long> transformar(EtlRequestDto<Salida, Long> archivo) {
		val result = super.transformar(archivo);

		val registros = result.getRegistros();

		for (val registro : registros) {
			cambiarDatosCantidad(registro);
		}

		return result;
	}

	private void cambiarDatosCantidad(RegistroDto<Salida, Long> registro) {
		String cantidad = registro.getDatos().get(Salida.CANTIDAD);
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < cantidad.length(); i++) {
			char c = cantidad.charAt(i);
			if (Character.isDigit(c)) {
				sb.append(c);
			} else {
				break;
			}
		}
		registro.getDatos().put(Salida.CANTIDAD, sb.toString());
	}
}