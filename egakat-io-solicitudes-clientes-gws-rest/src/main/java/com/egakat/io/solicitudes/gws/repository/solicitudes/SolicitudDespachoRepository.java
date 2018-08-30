package com.egakat.io.solicitudes.gws.repository.solicitudes;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.io.solicitudes.gws.domain.solicitudes.SolicitudDespacho;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.repository.DataQualityEntityRepository;
import com.egakat.io.solicitudes.gws.repository.ordenes.OrdenAlistamiento;
import com.egakat.io.solicitudes.gws.repository.ordenes.OrdenAlistamientoLinea;

public interface SolicitudDespachoRepository extends DataQualityEntityRepository<SolicitudDespacho> {

	@Override
	@Query("SELECT DISTINCT a.correlacion FROM SolicitudDespacho a WHERE a.estadoIntegracion IN (:estados) ORDER BY a.correlacion")
	List<String> findAllCorrelacionesByEstadoIntegracionIn(@Param("estados") List<EstadoIntegracionType> estados);

	
	
	
	// TODO esto debe estar en una entidad de ordenes de alistamiento
	// @formatter:off
	@Query(value = ""
			+ "SELECT "
			+ "	 a.id_solicitud_despacho AS id "
			+ "	,a.integracion "
			+ " ,a.id_externo AS idExterno "
			+ " ,a.correlacion "
			+ " ,a.id_orden_alistamiento AS idOrdenAlistamiento "
			+ "FROM [eIntegrationSolicitudes].[dbo].[OrdenesDeAlistamientoEnStage](:client_id,:ordnum,:wh_id) a", nativeQuery = true)
	// @formatter:on
	OrdenAlistamiento findOneOrdenDeAlistamientoEnStage(@Param("client_id") String client_id,
			@Param("ordnum") String ordnum, @Param("wh_id") String wh_id);

	// @formatter:off
	@Query(value = "SELECT " + 
			"     a.ordlin " + 
			"    ,a.numero_linea AS numeroLinea " + 
			"    ,a.numero_linea_externo AS numeroLineaExterno " + 
			"    ,a.numero_sublinea_externo AS numeroSublineaExterno " + 
			"    ,a.producto_codigo_alterno AS productoCodigoAlterno " + 
			"    ,a.producto_nombre AS productoNombre " + 
			"    ,a.bodega_codigo_alterno AS bodegaCodigoAlterno " + 
			"    ,a.estado_inventario_codigo_alterno AS estadoInventarioCodigoAlterno " + 
			"    ,a.cantidad " + 
			"FROM [eIntegrationSolicitudes].[dbo].[OrdenesDeAlistamientoEnStageLineas](:id_solicitud_despacho) a " + 
			"", nativeQuery = true)
	// @formatter:on
	List<OrdenAlistamientoLinea> findOrdenesDeAlistamientoEnStageLineas(
			@Param("id_solicitud_despacho") long id_solicitud_despacho);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE a SET cierre_notificado = 1 FROM [eConnect].dbo.ordenes_alistamiento a WHERE a.id_orden_alistamiento = :id_orden_alistamiento", nativeQuery = true)
	void updateEstadoNoficacion(@Param("id_orden_alistamiento") long id);
}
