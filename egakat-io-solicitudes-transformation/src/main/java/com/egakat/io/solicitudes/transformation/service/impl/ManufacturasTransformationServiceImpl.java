package com.egakat.io.solicitudes.transformation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.econnect.maestros.client.service.api.BodegaLocalService;
import com.egakat.econnect.maestros.client.service.api.ClienteLocalService;
import com.egakat.integration.commons.archivos.dto.ArchivoErrorDto;
import com.egakat.integration.commons.archivos.dto.CampoDto;
import com.egakat.io.commons.solicitudes.domain.manufacturas.Manufactura;
import com.egakat.io.commons.solicitudes.repository.manufacturas.ManufacturaRepository;
import com.egakat.io.solicitudes.transformation.service.api.ManufacturasTransformationService;
import com.egakat.wms.maestros.client.service.api.WhareHouseLocalService;

import lombok.val;

@Service
public class ManufacturasTransformationServiceImpl extends SolicitudesTransformationServiceImpl<Manufactura>
		implements ManufacturasTransformationService {
	@Autowired
	private ClienteLocalService clienteService;

	@Autowired
	private BodegaLocalService bodegaService;

	@Autowired
	private WhareHouseLocalService whService;

	@Autowired
	private ManufacturaRepository repository;

	@Override
	protected ManufacturaRepository getRepository() {
		return repository;
	}

	@Override
	protected boolean validateRow(Manufactura registro, List<ArchivoErrorDto> errores, List<CampoDto> campos) {
		boolean result = super.validateRow(registro, errores, campos);

		if (result) {
			if (registro.isRequiereBom()) {
				val cliente = clienteService.getByCodigo(registro.getClienteCodigo());
				val bodega = bodegaService.findOneById(registro.getIdBodega());

				val client_id = cliente.getCodigoAlternoWms();
				val wh_id = bodega.getCodigo();
				val prtnum = registro.getProductoCodigoAlterno();
				val invsts = registro.getEstadoInventarioCodigoAlterno();

				result = whService.productoTieneListaDeMateriales(wh_id, client_id, prtnum, invsts);
				if (!result) {
					val mensaje = "El producto no tiene asociada una lista de materiales";
					val datos = String.format("client_id=%s, wh_id=%s, prtnum=%s", client_id, wh_id, prtnum);
					val error = ArchivoErrorDto.error(registro.getIdArchivo(), mensaje, registro.getNumeroLinea(),
							datos);

					errores.add(error);
				}
			}
		}

		return result;
	}
}