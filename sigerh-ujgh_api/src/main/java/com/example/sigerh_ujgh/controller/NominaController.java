package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.dto.ReciboNominaDTO;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sigerh_ujgh.service.NominaService;
import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.repository.*;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/nomina")
@CrossOrigin("*")
public class NominaController {

    @Autowired
    private NominaService service;

    @Autowired
    private NominaRepository nominaRepository;
    // EL BOTÓN DE PAGO: Dispara el cálculo masivo
    @PostMapping("/calcular/{idLoteNomina}")
    public ResponseEntity<String> calcularNomina(@PathVariable Long idLoteNomina) {
        try {
            service.calcularNominaMasiva(idLoteNomina);
            return ResponseEntity.ok("Nomina calculada exitosamente para el Lote ID: " + idLoteNomina);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al calcular: " + e.getMessage());
        }
    }



    @GetMapping("/lote/{idLoteNomina}")
    public List<Nomina> listarPorLote(@PathVariable Long idLoteNomina) {
        return service.listarPorLote(idLoteNomina);
    }

    @GetMapping("/recibo/lote/{idLote}")
    public ResponseEntity<List<ReciboNominaDTO>> obtenerRecibosPorLote(@PathVariable Long idLote) {
        List<ReciboNominaDTO> recibos = service.obtenerReporteNomina(idLote);
        return ResponseEntity.ok(recibos);
    }

    @GetMapping("/reporte-pagos-excel/{id}")
    public ResponseEntity<byte[]> descargarReportePagosExcel(@PathVariable Long id) throws Exception {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(id);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reporte Pagos");

            // --- ESTILOS ---
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex()); // Color verde para pagos
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00"));

            // --- CABECERAS ---
            String[] columnas = {"N°", "CÉDULA", "NOMBRES Y APELLIDOS", "SUELDO BASE", "HORAS/BONOS EXTRA", "CESTA TICKET", "NETO A PAGAR (Bs)", "NETO A PAGAR ($)"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- LLENADO DE DATOS ---
            int rowIdx = 1;
            for (Nomina n : nominas) {
                Row row = sheet.createRow(rowIdx++);

                double sueldoBase = n.getSueldo_base() != null ? n.getSueldo_base().doubleValue() : 0.0;
                double cestaTicket = n.getMonto_cestaticket() != null ? n.getMonto_cestaticket().doubleValue() : 0.0;
                double totalIngresos = n.getTotal_ingreso() != null ? n.getTotal_ingreso().doubleValue() : 0.0;

                // Matemática para sacar los bonos extras: Total Ingresos menos Sueldo Base y Cesta Ticket
                double bonosExtra = totalIngresos - sueldoBase - cestaTicket;

                // El neto a pagar real sin las leyes restando
                double netoAPagarBs = sueldoBase + bonosExtra + cestaTicket;

                // Cálculo de dólares (si aplica)
                double tasaUsd = n.getLoteNomina().getTasa_bcv() != null ? n.getLoteNomina().getTasa_bcv().doubleValue() : 1.0;
                double netoAPagarUsd = tasaUsd > 0 ? (netoAPagarBs / tasaUsd) : 0.0;

                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue("V-" + n.getEmpleado().getPersona().getCedula());
                row.createCell(2).setCellValue(n.getEmpleado().getPersona().getNombre() + " " + n.getEmpleado().getPersona().getApellido());

                Cell cb = row.createCell(3); cb.setCellValue(sueldoBase); cb.setCellStyle(currencyStyle);
                Cell cbe = row.createCell(4); cbe.setCellValue(bonosExtra); cbe.setCellStyle(currencyStyle);
                Cell cct = row.createCell(5); cct.setCellValue(cestaTicket); cct.setCellStyle(currencyStyle);
                Cell cnb = row.createCell(6); cnb.setCellValue(netoAPagarBs); cnb.setCellStyle(currencyStyle);
                Cell cnu = row.createCell(7); cnu.setCellValue(netoAPagarUsd); cnu.setCellStyle(currencyStyle);
            }

            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "Reporte_Pagos_Lote_" + id + ".xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        }
    }

    @GetMapping("/reporte-leyes-excel/{id}")
    public ResponseEntity<byte[]> descargarReporteLeyesExcel(@PathVariable Long id) throws Exception {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(id);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reporte Leyes");

            // --- ESTILOS ---
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00"));

            // --- CABECERAS ---
            String[] columnas = {"N°", "CÉDULA", "NOMBRES Y APELLIDOS", "SUELDO BASE", "S.S.O (4%)", "S.P.F (0.5%)", "F.A.O.V (1%)", "CAJA AHORROS (10%)", "TOTAL DEDUCCIONES LEY"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- LLENADO DE DATOS ---
            int rowIdx = 1;
            for (Nomina n : nominas) {
                Row row = sheet.createRow(rowIdx++);

                double sueldoBase = n.getSueldo_base() != null ? n.getSueldo_base().doubleValue() : 0.0;
                double sso = n.getMonto_sso() != null ? n.getMonto_sso().doubleValue() : 0.0;
                double spf = n.getMonto_spf() != null ? n.getMonto_spf().doubleValue() : 0.0;
                double faov = n.getMonto_faov() != null ? n.getMonto_faov().doubleValue() : 0.0;

                // Cálculo de la Caja de Ahorros (10% del sueldo base)
                double cajaAhorros = n.getMontoCajaAhorro() != null ? n.getMontoCajaAhorro().doubleValue() : 0.0;
                double totalDeduccionesLey = sso + spf + faov + cajaAhorros;

                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue("V-" + n.getEmpleado().getPersona().getCedula());
                row.createCell(2).setCellValue(n.getEmpleado().getPersona().getNombre() + " " + n.getEmpleado().getPersona().getApellido());

                Cell cb = row.createCell(3); cb.setCellValue(sueldoBase); cb.setCellStyle(currencyStyle);
                Cell cs = row.createCell(4); cs.setCellValue(sso); cs.setCellStyle(currencyStyle);
                Cell cp = row.createCell(5); cp.setCellValue(spf); cp.setCellStyle(currencyStyle);
                Cell cf = row.createCell(6); cf.setCellValue(faov); cf.setCellStyle(currencyStyle);
                Cell cca = row.createCell(7); cca.setCellValue(cajaAhorros); cca.setCellStyle(currencyStyle);
                Cell ctot = row.createCell(8); ctot.setCellValue(totalDeduccionesLey); ctot.setCellStyle(currencyStyle);
            }

            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "Reporte_Leyes_Lote_" + id + ".xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        }
    }

    @GetMapping("/lotes/{id}/reporte-completo")
    public ResponseEntity<byte[]> generarSabanaNominaExcel(@PathVariable Long id) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Sábana de Nómina");

            // ==========================================
            // 1. CREACIÓN DE ESTILOS
            // ==========================================
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);
            textStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setBorderBottom(BorderStyle.THIN);
            currencyStyle.setBorderTop(BorderStyle.THIN);
            currencyStyle.setBorderLeft(BorderStyle.THIN);
            currencyStyle.setBorderRight(BorderStyle.THIN);
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00"));

            CellStyle totalStyle = workbook.createCellStyle();
            totalStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            totalStyle.setBorderBottom(BorderStyle.MEDIUM);
            totalStyle.setBorderTop(BorderStyle.MEDIUM);
            totalStyle.setBorderLeft(BorderStyle.THIN);
            totalStyle.setBorderRight(BorderStyle.THIN);
            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);
            totalStyle.setDataFormat(format.getFormat("#,##0.00"));

            // ==========================================
            // 2. CONSTRUCCIÓN DE ENCABEZADOS
            // ==========================================
            Row rowTitle = sheet.createRow(0);
            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue("SÁBANA COMPLETA DE NÓMINA - LOTE N° " + id);
            cellTitle.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));

            String[] columnas = {
                    "N°", "Cédula", "Nombres y Apellidos", "Cargo / Contrato",
                    "Sueldo Base", "Extras/Horas", "Cestaticket", "Total Ingresos",
                    "SSO", "FAOV", "S.P.F.", "Total Deducciones",
                    "Neto a Pagar (Bs)", "Tasa BCV", "Neto ($)"
            };

            Row headerRow = sheet.createRow(2);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // ==========================================
            // 3. LLENADO DE DATOS (DESDE LA BASE DE DATOS)
            // ==========================================
            // Buscamos todas las nóminas calculadas para este lote
            List<Nomina> listaNomina = nominaRepository.findByLoteNominaId(id);

            int rowNum = 3;
            int correlativo = 1;

            // Acumuladores para los totales de la fila final
            double sumSueldo = 0, sumExtras = 0, sumCestaT = 0, sumIngresos = 0;
            double sumSso = 0, sumFaov = 0, sumSpf = 0, sumDeducciones = 0;
            double sumNetoBs = 0, sumNetoUsd = 0;

            for (Nomina rec : listaNomina) {
                Row row = sheet.createRow(rowNum++);

                // 0. Correlativo
                Cell c0 = row.createCell(0); c0.setCellValue(correlativo++); c0.setCellStyle(textStyle);

                // 1. Cédula y 2. Nombres (Extrayendo de Persona)
                String cedula = "N/A";
                String nombres = "N/A";
                if(rec.getEmpleado() != null && rec.getEmpleado().getPersona() != null) {
                    cedula = rec.getEmpleado().getPersona().getCedula();
                    nombres = rec.getEmpleado().getPersona().getNombre() + " " + rec.getEmpleado().getPersona().getApellido();
                }
                Cell c1 = row.createCell(1); c1.setCellValue(cedula); c1.setCellStyle(textStyle);
                Cell c2 = row.createCell(2); c2.setCellValue(nombres); c2.setCellStyle(textStyle);

                // 3. Cargo / Contrato
                String contratoInfo = "N/A";
                if(rec.getId_contrato() != null && rec.getId_contrato().getIdTipoContrato() != null) {
                    contratoInfo = rec.getId_contrato().getIdTipoContrato().getNombre();
                }
                Cell c3 = row.createCell(3); c3.setCellValue(contratoInfo); c3.setCellStyle(textStyle);

                // 4. Sueldo Base
                double sueldo = rec.getSueldo_base() != null ? rec.getSueldo_base().doubleValue() : 0.0;
                Cell c4 = row.createCell(4); c4.setCellValue(sueldo); c4.setCellStyle(currencyStyle);
                sumSueldo += sueldo;

                // 5. Extras (Sumamos nocturnas, diurnas, fin de semana)
                double extras = (rec.getHora_diurna() != null ? rec.getHora_diurna().doubleValue() : 0.0) +
                        (rec.getHora_nocturna() != null ? rec.getHora_nocturna().doubleValue() : 0.0) +
                        (rec.getHora_fin() != null ? rec.getHora_fin().doubleValue() : 0.0);
                Cell c5 = row.createCell(5); c5.setCellValue(extras); c5.setCellStyle(currencyStyle);
                sumExtras += extras;

                // 6. Cestaticket
                double cesta = rec.getMonto_cestaticket() != null ? rec.getMonto_cestaticket().doubleValue() : 0.0;
                Cell c6 = row.createCell(6); c6.setCellValue(cesta); c6.setCellStyle(currencyStyle);
                sumCestaT += cesta;

                // 7. Total Ingresos
                double ingresos = rec.getTotal_ingreso() != null ? rec.getTotal_ingreso().doubleValue() : 0.0;
                Cell c7 = row.createCell(7); c7.setCellValue(ingresos); c7.setCellStyle(currencyStyle);
                sumIngresos += ingresos;

                // 8. SSO
                double sso = rec.getMonto_sso() != null ? rec.getMonto_sso().doubleValue() : 0.0;
                Cell c8 = row.createCell(8); c8.setCellValue(sso); c8.setCellStyle(currencyStyle);
                sumSso += sso;

                // 9. FAOV
                double faov = rec.getMonto_faov() != null ? rec.getMonto_faov().doubleValue() : 0.0;
                Cell c9 = row.createCell(9); c9.setCellValue(faov); c9.setCellStyle(currencyStyle);
                sumFaov += faov;

                // 10. SPF (Paro Forzoso)
                double spf = rec.getMonto_spf() != null ? rec.getMonto_spf().doubleValue() : 0.0;
                Cell c10 = row.createCell(10); c10.setCellValue(spf); c10.setCellStyle(currencyStyle);
                sumSpf += spf;

                // 11. Total Deducciones
                double deducciones = rec.getTotal_deducciones() != null ? rec.getTotal_deducciones().doubleValue() : 0.0;
                Cell c11 = row.createCell(11); c11.setCellValue(deducciones); c11.setCellStyle(currencyStyle);
                sumDeducciones += deducciones;

                // 12. Neto a Pagar Bs
                double netoBs = rec.getTotal_bs() != null ? rec.getTotal_bs().doubleValue() : 0.0;
                Cell c12 = row.createCell(12); c12.setCellValue(netoBs); c12.setCellStyle(currencyStyle);
                sumNetoBs += netoBs;

                // 13. Tasa BCV
                double tasa = rec.getTasabcv() != null ? rec.getTasabcv().doubleValue() : 0.0;
                Cell c13 = row.createCell(13); c13.setCellValue(tasa); c13.setCellStyle(currencyStyle);

                // 14. Neto a Pagar USD
                double netoUsd = rec.getNeto_a_pagar() != null ? rec.getNeto_a_pagar().doubleValue() : 0.0;
                Cell c14 = row.createCell(14); c14.setCellValue(netoUsd); c14.setCellStyle(currencyStyle);
                sumNetoUsd += netoUsd;
            }

            // ==========================================
            // 4. FILA DE TOTALES GENERALES
            // ==========================================
            Row rowTotales = sheet.createRow(rowNum);

            Cell cellTotalLabel = rowTotales.createCell(0);
            cellTotalLabel.setCellValue("TOTALES GENERALES DE LA NÓMINA");
            cellTotalLabel.setCellStyle(totalStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));
            for(int i=1; i<=3; i++) { rowTotales.createCell(i).setCellStyle(totalStyle); }

            rowTotales.createCell(4).setCellValue(sumSueldo); rowTotales.getCell(4).setCellStyle(totalStyle);
            rowTotales.createCell(5).setCellValue(sumExtras); rowTotales.getCell(5).setCellStyle(totalStyle);
            rowTotales.createCell(6).setCellValue(sumCestaT); rowTotales.getCell(6).setCellStyle(totalStyle);
            rowTotales.createCell(7).setCellValue(sumIngresos); rowTotales.getCell(7).setCellStyle(totalStyle);
            rowTotales.createCell(8).setCellValue(sumSso); rowTotales.getCell(8).setCellStyle(totalStyle);
            rowTotales.createCell(9).setCellValue(sumFaov); rowTotales.getCell(9).setCellStyle(totalStyle);
            rowTotales.createCell(10).setCellValue(sumSpf); rowTotales.getCell(10).setCellStyle(totalStyle);
            rowTotales.createCell(11).setCellValue(sumDeducciones); rowTotales.getCell(11).setCellStyle(totalStyle);
            rowTotales.createCell(12).setCellValue(sumNetoBs); rowTotales.getCell(12).setCellStyle(totalStyle);
            rowTotales.createCell(13).setCellValue("---"); rowTotales.getCell(13).setCellStyle(totalStyle); // No se suma la tasa
            rowTotales.createCell(14).setCellValue(sumNetoUsd); rowTotales.getCell(14).setCellStyle(totalStyle);

            // Autoajustar las columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "Sabana_Nomina_Completa_Lote_" + id + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}