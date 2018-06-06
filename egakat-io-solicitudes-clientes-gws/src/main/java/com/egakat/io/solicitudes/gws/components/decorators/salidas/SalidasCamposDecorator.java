package com.egakat.io.solicitudes.gws.components.decorators.salidas;

import org.apache.commons.lang3.StringUtils;

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
			val recaudo = (StringUtils.isNotEmpty(registro.getDatos().get(Salida.REQUIERE_RECAUDO)) ? "N" : "S");
			registro.getDatos().put(Salida.REQUIERE_RECAUDO,recaudo);
			
			cambiarDatosCantidad(registro);
			cambiarDatosHora(registro, Salida.HOMI);
			cambiarDatosHora(registro, Salida.HOMA);
			cambiarValorUnitarioDeclarado(registro);
		}

		return result;
	}

	private void cambiarDatosHora(RegistroDto<Salida, Long> registro, String key) {
		String hora = StringUtils.defaultString(registro.getDatos().get(key)).trim();

		if (hora.endsWith(":")) {
			hora = StringUtils.left(hora, hora.length() - 1);
		}
		if (hora.length() == 4) {
			hora = StringUtils.leftPad(hora, 5, "0");
		}
		if (hora.length() == 5) {
			if (hora.substring(2, 3).equals(".")) {
				hora = hora.substring(0, 2) + ":" + hora.substring(3, 5);
			}
		}

		registro.getDatos().put(key, hora);
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

	private void cambiarValorUnitarioDeclarado(RegistroDto<Salida, Long> registro) {
		String valor = StringUtils.defaultString(registro.getDatos().get(Salida.VALOR_UNITARIO_DECLARADO)).trim();

		if (valor.equals("-1")) {
			valor = "1";
			registro.getDatos().put(Salida.VALOR_UNITARIO_DECLARADO, valor);
		}
	}
}