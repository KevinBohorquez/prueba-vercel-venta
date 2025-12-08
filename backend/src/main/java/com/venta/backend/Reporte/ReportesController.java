package com.venta.backend.Reporte;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Endpoints para generación y descarga de reportes de ventas")
public class ReportesController {

    @Autowired
    private ReportesRepository reportesRepository;

    // --- OPCIÓN 1: VER DATOS (JSON) ---
    @GetMapping("/general")
    @Operation(summary = "Ver reporte en pantalla", description = "Devuelve la lista completa de ventas con detalles en JSON.")
    public ResponseEntity<List<ReporteVentaProjection>> verReporteJson() {
        List<ReporteVentaProjection> reporte = reportesRepository.obtenerReporteGeneral();
        return ResponseEntity.ok(reporte);
    }

    // --- OPCIÓN 2: DESCARGAR EXCEL COMPLETO ---
    @GetMapping("/descargar-excel")
    @Operation(summary = "Descargar Excel Completo", description = "Genera y descarga un archivo .xlsx con TODAS las columnas disponibles.")
    public ResponseEntity<byte[]> descargarExcel() throws IOException {

        List<ReporteVentaProjection> data = reportesRepository.obtenerReporteGeneral();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte General");

            // --- ESTILOS ---
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Estilos de datos
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            // --- CABECERAS (TODAS LAS COLUMNAS) ---
            String[] columnas = {
                    // A. CLIENTE
                    "ID Cliente", "DNI/RUC", "Nombre Cliente", "Email", "Categoría", "Distrito", "Canal Favorito",
                    // B. MÉTRICAS
                    "Ventas Históricas", "Monto Acumulado", "Desc. Acumulado", "Última Compra", "Total Cotizaciones", "Cotiz. Aceptadas",
                    // C. TRANSACCIÓN
                    "ID Venta", "Comprobante", "Fecha Venta", "Estado", "Método Pago", "Origen",
                    // D. DETALLE
                    "Producto", "Tipo Prod.", "Cantidad", "Precio Unit.", "Desc. Item",
                    // E. ATRIBUCIÓN
                    "Vendedor", "Tipo Vendedor", "Sede", "Campaña Lead",
                    // F. TOTALES & CONTRATOS
                    "Subtotal Venta", "Desc. Total Venta", "Total Final", "¿Es Contrato?", "Num. Contrato", "Monto Mensual"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- LLENADO DE DATOS ---
            int rowIdx = 1;
            for (ReporteVentaProjection fila : data) {
                Row row = sheet.createRow(rowIdx++);
                int colIdx = 0;

                // A. CLIENTE
                row.createCell(colIdx++).setCellValue(fila.getCliente_id() != null ? fila.getCliente_id() : 0);
                row.createCell(colIdx++).setCellValue(fila.getDni() != null ? fila.getDni() : "-");
                row.createCell(colIdx++).setCellValue(fila.getNombreCliente() != null ? fila.getNombreCliente() : "Anónimo");
                row.createCell(colIdx++).setCellValue(fila.getEmailCliente() != null ? fila.getEmailCliente() : "-");
                row.createCell(colIdx++).setCellValue(fila.getCategoriaFidelidad() != null ? fila.getCategoriaFidelidad() : "-");
                row.createCell(colIdx++).setCellValue(fila.getDistritoPrincipal() != null ? fila.getDistritoPrincipal() : "-");
                row.createCell(colIdx++).setCellValue(fila.getCanalContactoFavorito() != null ? fila.getCanalContactoFavorito() : "-");

                // B. MÉTRICAS
                row.createCell(colIdx++).setCellValue(fila.getMetricaTotalVentas() != null ? fila.getMetricaTotalVentas() : 0);

                Cell cMontoAcum = row.createCell(colIdx++);
                cMontoAcum.setCellValue(fila.getMetricaMontoAcum() != null ? fila.getMetricaMontoAcum() : 0.0);
                cMontoAcum.setCellStyle(currencyStyle);

                Cell cDescAcum = row.createCell(colIdx++);
                cDescAcum.setCellValue(fila.getMetricaDescAcum() != null ? fila.getMetricaDescAcum() : 0.0);
                cDescAcum.setCellStyle(currencyStyle);

                Cell cFechaUlt = row.createCell(colIdx++);
                if(fila.getMetricaFechaUltimaCompra() != null) {
                    cFechaUlt.setCellValue(fila.getMetricaFechaUltimaCompra());
                    cFechaUlt.setCellStyle(dateStyle);
                } else {
                    cFechaUlt.setCellValue("-");
                }

                row.createCell(colIdx++).setCellValue(fila.getMetricaTotalCotizaciones() != null ? fila.getMetricaTotalCotizaciones() : 0);
                row.createCell(colIdx++).setCellValue(fila.getMetricaCotizAceptadas() != null ? fila.getMetricaCotizAceptadas() : 0);

                // C. TRANSACCIÓN
                row.createCell(colIdx++).setCellValue(fila.getVentaId() != null ? fila.getVentaId() : 0);
                row.createCell(colIdx++).setCellValue(fila.getNumComprobante() != null ? fila.getNumComprobante() : "-");

                Cell cFechaVenta = row.createCell(colIdx++);
                if(fila.getFechaVenta() != null) {
                    cFechaVenta.setCellValue(fila.getFechaVenta());
                    cFechaVenta.setCellStyle(dateStyle);
                } else {
                    cFechaVenta.setCellValue("-");
                }

                row.createCell(colIdx++).setCellValue(fila.getEstadoVenta() != null ? fila.getEstadoVenta() : "-");
                row.createCell(colIdx++).setCellValue(fila.getMetodoPago() != null ? fila.getMetodoPago() : "-");
                row.createCell(colIdx++).setCellValue(fila.getOrigenVenta() != null ? fila.getOrigenVenta() : "-");

                // D. DETALLE
                row.createCell(colIdx++).setCellValue(fila.getNombreProducto() != null ? fila.getNombreProducto() : "-");
                row.createCell(colIdx++).setCellValue(fila.getTipoProducto() != null ? fila.getTipoProducto() : "-");
                row.createCell(colIdx++).setCellValue(fila.getCantidadVendida() != null ? fila.getCantidadVendida() : 0);

                Cell cPrecioUnit = row.createCell(colIdx++);
                cPrecioUnit.setCellValue(fila.getPrecioUnitarioVenta() != null ? fila.getPrecioUnitarioVenta() : 0.0);
                cPrecioUnit.setCellStyle(currencyStyle);

                Cell cDescItem = row.createCell(colIdx++);
                cDescItem.setCellValue(fila.getDescuentoMontoItem() != null ? fila.getDescuentoMontoItem() : 0.0);
                cDescItem.setCellStyle(currencyStyle);

                // E. ATRIBUCIÓN
                row.createCell(colIdx++).setCellValue(fila.getVendedorNombre() != null ? fila.getVendedorNombre() : "Sin Asignar");
                row.createCell(colIdx++).setCellValue(fila.getVendedorTipo() != null ? fila.getVendedorTipo() : "-");
                row.createCell(colIdx++).setCellValue(fila.getSedeVenta() != null ? fila.getSedeVenta() : "-");
                row.createCell(colIdx++).setCellValue(fila.getLeadCampania() != null ? fila.getLeadCampania() : "Orgánico");

                // F. TOTALES & CONTRATOS
                Cell cSubtotal = row.createCell(colIdx++);
                cSubtotal.setCellValue(fila.getSubtotalVenta() != null ? fila.getSubtotalVenta() : 0.0);
                cSubtotal.setCellStyle(currencyStyle);

                Cell cDescTotal = row.createCell(colIdx++);
                cDescTotal.setCellValue(fila.getDescuentoTotalVenta() != null ? fila.getDescuentoTotalVenta() : 0.0);
                cDescTotal.setCellStyle(currencyStyle);

                Cell cTotal = row.createCell(colIdx++);
                cTotal.setCellValue(fila.getTotalFinalVenta() != null ? fila.getTotalFinalVenta() : 0.0);
                cTotal.setCellStyle(currencyStyle);

                // Contratos
                boolean esContrato = fila.getEsContratoRecurrente() != null && fila.getEsContratoRecurrente() == 1;
                row.createCell(colIdx++).setCellValue(esContrato ? "SÍ" : "NO");
                row.createCell(colIdx++).setCellValue(fila.getContratoNumero() != null ? fila.getContratoNumero() : "-");

                Cell cMontoContrato = row.createCell(colIdx++);
                if (fila.getContratoMontoMensual() != null) {
                    cMontoContrato.setCellValue(fila.getContratoMontoMensual());
                    cMontoContrato.setCellStyle(currencyStyle);
                } else {
                    cMontoContrato.setCellValue("-");
                }
            }

            // AUTOAJUSTAR COLUMNAS (Solo las primeras para no tardar mucho si hay miles de filas)
            for (int i = 0; i < columnas.length; i++) {
                sheet.setColumnWidth(i, 5000); // Ancho fijo moderado para rendimiento
            }

            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Completo_" + System.currentTimeMillis() + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());
        }
    }
}