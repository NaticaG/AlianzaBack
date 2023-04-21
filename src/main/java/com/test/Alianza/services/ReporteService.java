package com.test.Alianza.services;

import com.test.Alianza.entity.Cliente;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ClienteService clienteService;

    public ByteArrayInputStream generarReporte() throws Exception {
        System.out.println("[ReporteService][generarReporteServicios]Inicio");
        List<Cliente> listadoClientes = clienteService.listarClientes();

        String[] columns = {"sharedKey","name","email",
                "phone","startDate","endDate","addDate"};

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Workbook workbook = new HSSFWorkbook();
        CellStyle style = setCellStyle(workbook, (short) 14, IndexedColors.WHITE.getIndex(), false, false, false,
                HorizontalAlignment.CENTER, null);

        int initRow = 0;
        Sheet sheet = workbook.createSheet("Reporte");
        Row row = sheet.createRow(initRow);
        initRow ++;

        // Creación de los encabezados
        row = sheet.createRow(initRow);
        initRow ++;
        for (int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }


        // Lllenar los Campos del Excel
        for (Cliente cli : listadoClientes) {
            row = sheet.createRow(initRow);

            row.createCell(0).setCellValue(cli.getSharedKey());
            row.createCell(1).setCellValue(cli.getName());
            row.createCell(2).setCellValue(cli.getEmail());
            row.createCell(3).setCellValue(cli.getPhone());
            row.createCell(4).setCellValue(cli.getStartDate());
            row.createCell(5).setCellValue(cli.getEndDate());
            row.createCell(6).setCellValue(cli.getAddDate());
            initRow ++;
        }
        workbook.write(stream);
        workbook.close();

        return new ByteArrayInputStream(stream.toByteArray());
    }

    protected CellStyle setCellStyle(Workbook book, short fontsize, Short color, boolean isBold, boolean border,
                                     boolean backGround, HorizontalAlignment horizontalAlign, VerticalAlignment verticalAlign) {

        CellStyle cellStyle = book.createCellStyle();
        Font headersFont = book.createFont();

        headersFont.setFontHeightInPoints(fontsize);
        headersFont.setColor(IndexedColors.BLACK.getIndex());
        headersFont.setBold((isBold));
        cellStyle.setFont(headersFont);
        if (horizontalAlign != null) {
            cellStyle.setAlignment(horizontalAlign);
        }
        if (verticalAlign != null) {
            cellStyle.setVerticalAlignment(verticalAlign);
        }

        return cellStyle;
    }
}
