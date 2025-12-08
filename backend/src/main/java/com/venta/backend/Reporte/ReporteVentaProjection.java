
package com.venta.backend.Reporte;

import java.util.Date;

public interface ReporteVentaProjection {
    // --- A. CLIENTE ---
    Long getCliente_id();
    String getDni();
    String getNombreCliente();
    String getEmailCliente();
    String getCategoriaFidelidad();
    String getDistritoPrincipal();
    String getCanalContactoFavorito();

    // --- B. MÉTRICAS ---
    Integer getMetricaTotalVentas();
    Double getMetricaMontoAcum();
    Double getMetricaDescAcum();
    Date getMetricaFechaUltimaCompra();
    Integer getMetricaTotalCotizaciones();
    Integer getMetricaCotizAceptadas();

    // --- C. TRANSACCIÓN ---
    Long getVentaId();
    String getNumComprobante();
    Date getFechaVenta();
    String getEstadoVenta();
    String getMetodoPago();
    String getOrigenVenta();

    // --- D. DETALLE ---
    Long getDetalleVentaId();
    Long getProductoId();
    String getNombreProducto();
    String getTipoProducto();
    Integer getCantidadVendida();
    Double getPrecioUnitarioVenta();
    Double getDescuentoMontoItem();

    // --- E. ATRIBUCIÓN ---
    String getVendedorNombre();
    String getVendedorTipo();
    String getSedeVenta();
    String getSedeTipo();
    String getLeadCanalOrigen();
    String getLeadCampania();

    // --- F. TOTALES ---
    Double getSubtotalVenta();
    Double getDescuentoTotalVenta();
    Double getTotalFinalVenta();

    // --- G. CONTRATOS ---
    Integer getEsContratoRecurrente();
    String getContratoNumero();
    Double getContratoMontoMensual();

    // --- H. CAMPOS AGREGADOS PARA EL REPORTE EXCEL ---
    // Estos son los que faltaban y causaban el error en el Controller
    String getHuboDescuento();      // Devuelve "SI" o "NO" (o lo que mande tu SQL)
    Double getMontoDescontado();    // Devuelve el monto para la columna reporte
}