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
import java.util.Objects;

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

    // --- OPCIÓN 2: DESCARGAR EXCEL (AGRUPADO VISUALMENTE) ---
    @GetMapping("/descargar-excel")
    @Operation(summary = "Descargar Excel Completo", description = "Genera y descarga un archivo .xlsx con agrupación visual de ventas.")
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
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            // --- CABECERAS ---
            String[] columnas = {
                    // Cabecera de la Venta (Datos Generales)
                    "ID Venta", "Fecha", "Cliente", "DNI/RUC", "Email",
                    // Detalle del Producto (Esto cambia en cada fila)
                    "Producto", "Tipo", "Cant.", "Precio Unit.", "Subtotal Item",
                    // Atribución y Totales (Datos Generales)
                    "Vendedor", "Sede", "Total Venta", "¿Contrato?", "Monto Mensual"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- VARIABLES DE CONTROL PARA AGRUPACIÓN VISUAL ---
            Long lastVentaId = null;
            int rowIdx = 1;

            for (ReporteVentaProjection fila : data) {
                Row row = sheet.createRow(rowIdx++);
                int colIdx = 0;

                // Detectamos si es una nueva venta comparando el ID con la fila anterior
                boolean isNewSale = !Objects.equals(fila.getVentaId(), lastVentaId);

                // 1. DATOS DE CABECERA (Solo se imprimen si es una nueva venta)
                if (isNewSale) {
                    row.createCell(colIdx++).setCellValue(fila.getVentaId() != null ? fila.getVentaId() : 0);

                    Cell cFecha = row.createCell(colIdx++);
                    if(fila.getFechaVenta() != null) {
                        cFecha.setCellValue(fila.getFechaVenta());
                        cFecha.setCellStyle(dateStyle);
                    } else { cFecha.setCellValue("-"); }

                    row.createCell(colIdx++).setCellValue(fila.getNombreCliente() != null ? fila.getNombreCliente() : "Anónimo");
                    row.createCell(colIdx++).setCellValue(fila.getDni() != null ? fila.getDni() : "-");
                    row.createCell(colIdx++).setCellValue(fila.getEmailCliente() != null ? fila.getEmailCliente() : "-");
                } else {
                    // Si es la misma venta, dejamos estas celdas vacías
                    colIdx += 5; // Saltamos 5 columnas
                }

                // 2. DATOS DE PRODUCTO (Se imprimen SIEMPRE, fila a fila)
                row.createCell(colIdx++).setCellValue(fila.getNombreProducto() != null ? fila.getNombreProducto() : "-");
                row.createCell(colIdx++).setCellValue(fila.getTipoProducto() != null ? fila.getTipoProducto() : "-");
                row.createCell(colIdx++).setCellValue(fila.getCantidadVendida() != null ? fila.getCantidadVendida() : 0);

                Cell cPrecio = row.createCell(colIdx++);
                cPrecio.setCellValue(fila.getPrecioUnitarioVenta() != null ? fila.getPrecioUnitarioVenta() : 0.0);
                cPrecio.setCellStyle(currencyStyle);

                // Cálculo simple de subtotal item
                Cell cSubItem = row.createCell(colIdx++);
                double subTotalCalc = (fila.getPrecioUnitarioVenta() != null ? fila.getPrecioUnitarioVenta() : 0.0)
                        * (fila.getCantidadVendida() != null ? fila.getCantidadVendida() : 0);
                cSubItem.setCellValue(subTotalCalc);
                cSubItem.setCellStyle(currencyStyle);

                // 3. ATRIBUCIÓN Y TOTALES (Solo se imprimen si es una nueva venta)
                if (isNewSale) {
                    row.createCell(colIdx++).setCellValue(fila.getVendedorNombre() != null ? fila.getVendedorNombre() : "-");
                    row.createCell(colIdx++).setCellValue(fila.getSedeVenta() != null ? fila.getSedeVenta() : "-");

                    Cell cTotal = row.createCell(colIdx++);
                    cTotal.setCellValue(fila.getTotalFinalVenta() != null ? fila.getTotalFinalVenta() : 0.0);
                    cTotal.setCellStyle(currencyStyle);

                    boolean esContrato = fila.getEsContratoRecurrente() != null && fila.getEsContratoRecurrente() == 1;
                    row.createCell(colIdx++).setCellValue(esContrato ? "SÍ" : "NO");

                    Cell cMontoM = row.createCell(colIdx++);
                    if(fila.getContratoMontoMensual() != null) {
                        cMontoM.setCellValue(fila.getContratoMontoMensual());
                        cMontoM.setCellStyle(currencyStyle);
                    } else { cMontoM.setCellValue("-"); }

                } else {
                    // Celdas vacías para el resto
                    // No hace falta rellenar con "", Excel las asume vacías si no escribimos nada.
                }

                // Guardamos el ID actual para compararlo en la siguiente vuelta
                lastVentaId = fila.getVentaId();
            }

            // Ancho de columnas fijo para evitar lentitud
            for (int i = 0; i < columnas.length; i++) {
                sheet.setColumnWidth(i, 4500);
            }

            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Ventas_Agrupado_" + System.currentTimeMillis() + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());
        }
    }
}