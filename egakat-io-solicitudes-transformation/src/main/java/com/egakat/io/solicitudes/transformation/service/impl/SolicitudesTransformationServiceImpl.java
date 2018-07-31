package com.egakat.io.solicitudes.transformation.service.impl;

import static com.egakat.io.solicitudes.domain.Solicitud.BODEGA_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.CLIENTE_CODIGO;
import static com.egakat.io.solicitudes.domain.Solicitud.ESTADO_INVENTARIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.PRODUCTO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.SERVICIO_CODIGO_ALTERNO;
import static com.egakat.io.solicitudes.domain.Solicitud.UNIDAD_MEDIDA_CODIGO_ALTERNO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.egakat.econnect.maestros.client.service.api.lookup.LookUpService;
import com.egakat.integration.core.transformation.service.impl.TransformationServiceImpl;
import com.egakat.integration.files.dto.CampoDto;
import com.egakat.io.solicitudes.domain.Solicitud;

import lombok.val;

public abstract class SolicitudesTransformationServiceImpl<T extends Solicitud> extends TransformationServiceImpl<T> {

	@Autowired
	private LookUpService lookUpService;

	protected LookUpService getLookUpService() {
		return lookUpService;
	}
	
	@Override
	public void cacheEvict() {
		getLookUpService().cacheEvict();
	}

	public SolicitudesTransformationServiceImpl() {
		super();
	}

	@Override
	protected void translateField(T registro, CampoDto campo, String value) {
		switch (campo.getCodigo()) {
		case CLIENTE_CODIGO:
			translateCliente(registro, value);
			break;
		case SERVICIO_CODIGO_ALTERNO:
			translateServicio(registro, value);
			break;
		case PRODUCTO_CODIGO_ALTERNO:
			translateProducto(registro, value);
			break;
		case UNIDAD_MEDIDA_CODIGO_ALTERNO:
			if (StringUtils.isEmpty(value)) {
				if (!campo.isIncluir()) {
					break;
				}
			}
			translateUnidadMedida(registro, value);
			break;
		case BODEGA_CODIGO_ALTERNO:
			translateBodega(registro, value);
			break;
		case ESTADO_INVENTARIO_CODIGO_ALTERNO:
			translateEstadoInventario(registro, value);
			break;
		default:
		}
	}

	protected void translateCliente(T registro, String value) {
		registro.setIdCliente(null);
		val id = getLookUpService().findClienteIdByCodigo(value);
		registro.setIdCliente(id);
	}

	protected void translateServicio(T registro, String value) {
		registro.setIdServicio(null);
		val id = getLookUpService().findServicioIdByCodigo(value);
		registro.setIdServicio(id);
	}

	protected void translateProducto(T registro, String value) {
		registro.setIdProducto(null);
		val cliente = registro.getIdCliente();
		if (cliente != null) {
			val id = getLookUpService().findProductoIdByClienteIdAndCodigo(cliente.longValue(), value);
			registro.setIdProducto(id);
		}
	}

	protected void translateUnidadMedida(T registro, String value) {
		registro.setIdUnidadMedida(null);
		Long id = null;

		if (!"".equals(value)) {
			id = getLookUpService().findUnidadMedidaIdByCodigo(value);
		} else {
			val producto = registro.getIdProducto();
			val bodega = registro.getIdBodega();
			if ((producto != null) && (bodega != null)) {
				id = getLookUpService().findUnidadMedidaDeReciboIdByProductoIdAndBodegaId(producto, bodega);
			}
		}

		registro.setIdUnidadMedida(id);
	}

	protected void translateBodega(T registro, String value) {
		registro.setIdBodega(null);
		val id = getLookUpService().findBodegaIdByCodigo(value);
		registro.setIdBodega(id);
	}

	protected void translateEstadoInventario(T registro, String value) {
		registro.setIdEstadoInventario(null);
		val id = getLookUpService().findEstadoInventarioIdByCodigo(value);
		registro.setIdEstadoInventario(id);
	}
}