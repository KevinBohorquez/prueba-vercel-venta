package com.venta.backend.Reporte;

import com.venta.backend.venta.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportesRepository extends JpaRepository<Venta, Long> {

    @Query(value = """
        SELECT
            -- A. CLIENTE
            c.cliente_id,
            c.dni,
            CONCAT(c.first_name, ' ', c.last_name) AS nombreCliente,
            c.email AS emailCliente,
            c.categoria AS categoriaFidelidad,
            dir.distrito AS distritoPrincipal,
            pref.canal_contacto_favorito AS canalContactoFavorito,

            -- B. MÉTRICAS HISTÓRICAS
            IFNULL(cm.total_ventas_historicas, 0) AS metricaTotalVentas,
            IFNULL(cm.total_monto_acumulado, 0) AS metricaMontoAcum,
            IFNULL(cm.total_descuento_acumulado, 0) AS metricaDescAcum,
            cm.fecha_ultima_compra AS metricaFechaUltimaCompra,
            IFNULL(cc.total_cotizaciones_realizadas, 0) AS metricaTotalCotizaciones,
            IFNULL(cc.cotizaciones_aceptadas, 0) AS metricaCotizAceptadas,

            -- C. TRANSACCIÓN
            v.id_venta AS ventaId,
            v.num_venta AS numComprobante,
            v.fecha_venta_creada AS fechaVenta,
            v.estado AS estadoVenta,
            v.metodo_pago AS metodoPago,
            v.origen_venta AS origenVenta,

            -- D. DETALLE DE PRODUCTO
            dv.id_detalle_venta AS detalleVentaId,
            dv.id_producto AS productoId,
            dv.nombre_producto AS nombreProducto,
            p.tipo AS tipoProducto,
            dv.cantidad AS cantidadVendida,
            dv.precio_unitario AS precioUnitarioVenta,
            dv.descuento_monto AS descuentoMontoItem,

            -- E. ATRIBUCIÓN Y VENDEDOR
            CONCAT(vendedor.first_name, ' ', vendedor.last_name) AS vendedorNombre,
            vendedor.seller_type AS vendedorTipo,
            sede.name AS sedeVenta,
            sede.type AS sedeTipo,
            vl.canal_origen AS leadCanalOrigen,
            vl.nombre_campania AS leadCampania,

            -- F. TOTALES
            v.subtotal AS subtotalVenta,
            v.descuento_total AS descuentoTotalVenta,
            v.total AS totalFinalVenta,
            CASE WHEN cont.id_contrato IS NOT NULL THEN 1 ELSE 0 END AS esContratoRecurrente,
            cont.num_contrato AS contratoNumero,
            cont.monto_mensual AS contratoMontoMensual,
            
            -- G. CAMPOS ADICIONALES PARA EL CONTROLADOR EXCEL
            CASE WHEN v.descuento_total > 0 THEN 'SI' ELSE 'NO' END AS huboDescuento,
            IFNULL(v.descuento_total, 0) AS montoDescontado

        FROM detalle_venta dv

        JOIN venta v ON dv.id_venta = v.id_venta
        JOIN cliente c ON v.cliente_id = c.cliente_id
        LEFT JOIN direccion dir ON c.cliente_id = dir.id_cliente AND dir.es_principal = 1
        LEFT JOIN preferencia pref ON c.cliente_id = pref.id_cliente

        LEFT JOIN productos p ON dv.id_producto = p.id
        LEFT JOIN vendedores vendedor ON v.id_vendedor = vendedor.seller_id
        LEFT JOIN sedes sede ON vendedor.id_sede = sede.branch_id

        LEFT JOIN (
            SELECT
                id_cliente,
                COUNT(DISTINCT id_venta) AS total_ventas_historicas,
                SUM(monto_total) AS total_monto_acumulado,
                SUM(descuento_aplicado) AS total_descuento_acumulado,
                MAX(fecha_compra) AS fecha_ultima_compra
            FROM historial_compras
            GROUP BY id_cliente
        ) cm ON c.cliente_id = cm.id_cliente

        LEFT JOIN (
            SELECT
                cliente_id,
                COUNT(id) AS total_cotizaciones_realizadas,
                SUM(CASE WHEN estado = 'ACEPTADA' THEN 1 ELSE 0 END) AS cotizaciones_aceptadas
            FROM cotizacion
            GROUP BY cliente_id
        ) cc ON c.cliente_id = cc.cliente_id

        LEFT JOIN contrato cont ON v.id_venta = cont.id_venta
        LEFT JOIN venta_lead vl ON v.id_venta = vl.id_venta

        ORDER BY c.cliente_id, v.fecha_venta_creada DESC, dv.id_detalle_venta
        """, nativeQuery = true)
    List<ReporteVentaProjection> obtenerReporteGeneral();
}