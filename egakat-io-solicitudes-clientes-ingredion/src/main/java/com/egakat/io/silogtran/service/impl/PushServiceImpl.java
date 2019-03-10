package com.egakat.io.silogtran.service.impl;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.left;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.NestedRuntimeException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.egakat.core.web.client.exception.ReintentableException;
import com.egakat.io.ingredion.dto.ErrorDto;
import com.egakat.io.ingredion.dto.Reintentable;
import com.egakat.io.silogtran.service.api.PushService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PushServiceImpl<ID,I extends Reintentable<ID>, O, R> implements PushService {

	protected abstract String getIntegracion();

	protected String getOperacion() {
		val result = String.format("PUSH %s", getIntegracion());
		return result;
	}

	@Override
	public boolean push() {
		boolean result = true;
		val operacion = getOperacion();

		val inputs = getPendientes();
		int i = 1;
		int n = inputs.size();

		log.info("{}: numero de pendientes={}", operacion, n);

		if (n > 0) {
			init(inputs);

			for (val input : inputs) {
				val errores = new ArrayList<ErrorDto>();
				boolean retry = false;

				try {
					log(input, i, n);

					push(input, errores);
				} catch (RuntimeException e) {
					if (retry(input, e)) {
						retry = true;
						input.setReintentos(input.getReintentos() + 1);
					}

					val error = error(input, "", retry, e);
					errores.add(error);
				}

				if (!errores.isEmpty()) {
					if (retry) {
						result = false;
						onRetry(input, errores);
						updateOnRetry(input, errores);
					} else {
						onError(input, errores);
						updateOnError(input, errores);
					}
				}

				i++;
			}
		}
		return result;
	}

	protected void init(List<I> inputs) {

	}

	protected abstract List<I> getPendientes();

	protected void push(I input, List<ErrorDto> errores) {
		validateInput(input, errores);

		if (errores.isEmpty()) {
			O output = asOutput(input, errores);

			if (errores.isEmpty()) {
				validateOutput(output, input, errores);

				if (errores.isEmpty()) {
					val discard = shouldBeDiscarded(output, input);

					if (!discard) {
						R result = push(output, input, errores);

						if (errores.isEmpty()) {
							onSuccess(result, output, input);
							updateOnSuccess(result, output, input);
						}
					} else {
						onDiscard(output, input);
						updateOnDiscard(output, input);
					}
				}
			}
		}
	}

	protected void validateInput(I input, List<ErrorDto> errores) {

	}

	protected abstract O asOutput(I input, List<ErrorDto> errores);

	protected void validateOutput(O output, I input, List<ErrorDto> errores) {

	}

	protected boolean shouldBeDiscarded(O output, I input) {
		return false;
	}

	protected abstract R push(O output, I input, List<ErrorDto> errores);

	protected abstract void onSuccess(R result, O output, I input);

	protected abstract void updateOnSuccess(R result, O output, I input);

	protected void onDiscard(O output, I input) {

	}

	protected void updateOnDiscard(O output, I input) {

	}

	protected abstract void onError(I input, List<ErrorDto> errores);

	protected abstract void updateOnError(I input, List<ErrorDto> errores);

	protected void onRetry(I input, List<ErrorDto> errores) {

	}

	protected abstract void updateOnRetry(I input, List<ErrorDto> errores);

	protected boolean retry(I input, RuntimeException e) {
		boolean result = false;

		if (input.getReintentos() < getNumeroMaximoIntentos()) {
			if (e instanceof ReintentableException) {
				result = true;
			}

			if (e instanceof ResourceAccessException) {
				result = true;
			}
		}

		return result;
	}

	protected int getNumeroMaximoIntentos() {
		return 30;
	}

	protected void log(I input, int i, int n) {
		if (input != null) {
			val format = "{}: Procesando {} de {}: id externo={}, contenido={} ";
			log.debug(format, getOperacion(), i, n, input.getIdExterno(), input);
		}
	}

	protected String normalizar(String str, int len) {
		val result = left(defaultString(str), len);
		return result;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// --
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ErrorDto error(I input, String codigo, boolean ignorar, Throwable t) {
		val result = error(getOperacion(), input.getIdExterno(), codigo, ignorar, t);
		return result;
	}

	protected ErrorDto error(String integracion, String idExterno, String codigo, boolean ignorar, Throwable t) {
		String c = isEmpty(codigo) ? t.getClass().getName() : codigo;
		String msg = t.getMessage();

		if (t instanceof HttpStatusCodeException) {
			val e = (HttpStatusCodeException) t;
			c = e.getStatusCode().toString();
			msg = e.getResponseBodyAsString();
		} else {
			if (t instanceof NestedRuntimeException) {
				val e = (NestedRuntimeException) t;
				if (e.getMostSpecificCause() != null) {
					msg = e.getMostSpecificCause().getMessage();
				}
			}
		}

		val result = error(integracion, idExterno, c, ignorar, msg);
		return result;
	}

	protected ErrorDto error(String integracion, String idExterno, String codigo, boolean ignorar, String mensaje,
			String... arg) {
		// val argumentos = normalizarArgumentos(arg);

		val result = new ErrorDto();
		val estado = (ignorar) ? "IGNORAR" : "NOTIFICAR";
		val now = LocalDateTime.now();
		
		// result.setIntegracion(integracion);
		// result.setCorrelacion(correlacion);
		// result.setIdExterno(idExterno);
		result.setEstadoNotificacion(estado);
		result.setCodigo(left(codigo, 100));
		result.setMensaje(defaultString(mensaje));
		result.setFechaCreacion(now);
		result.setFechaModificacion(now);
		return result;
	}

	protected static String[] normalizarArgumentos(String... arg) {
		val result = new String[10];

		int n = Math.min(result.length, arg.length);
		for (int i = 0; i < result.length; i++) {
			if (i < n) {
				result[i] = arg[i];
			} else {
				result[i] = "";
			}
		}

		return result;
	}

}