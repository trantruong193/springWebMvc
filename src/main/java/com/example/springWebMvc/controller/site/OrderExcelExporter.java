package com.example.springWebMvc.controller.site;

import com.example.springWebMvc.persistent.dto.OrderDTO;
import com.example.springWebMvc.persistent.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderExcelExporter {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private OrderDTO dto;

    public OrderExcelExporter(OrderDTO dto) {
        this.dto = dto;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Orders");
    }

    private void writeOrderHeader(){
        Row row = sheet.createRow(0);
        // set font
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        // insert cell
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Order Code");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Name");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("Phone");

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("Address");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Email");

        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("Order Time");

        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Price");
    }
    private void writeProductHeader(){
        Row row = sheet.createRow(2);
        // set font
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        // insert cell
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Product Name");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Type");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("Color");

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("Quantity");
    }
    private void writeOrderDetail(){
        Row row = sheet.createRow(1);

        Cell cell = row.createCell(0);
        cell.setCellValue(dto.getOrderCode());
        sheet.autoSizeColumn(0);

        cell = row.createCell(1);
        cell.setCellValue(dto.getCusName());
        sheet.autoSizeColumn(1);

        cell = row.createCell(2);
        cell.setCellValue(dto.getPhone());
        sheet.autoSizeColumn(2);

        cell = row.createCell(3);
        cell.setCellValue(dto.getAddress());
        sheet.autoSizeColumn(3);

        cell = row.createCell(4);
        cell.setCellValue(dto.getEmail());
        sheet.autoSizeColumn(4);

        cell = row.createCell(5);
        cell.setCellValue(dateFormat.format(dto.getCreateTime()));
        sheet.autoSizeColumn(5);

        cell = row.createCell(6);
        cell.setCellValue(dto.getTotalPrice()+" $");
    }
    private void writeProductDetail(){
        int rowCount = 3;
        for (OrderDetailDTO p: dto.getOrderDetailDTOList()){
            Row row = sheet.createRow(rowCount);

            Cell cell = row.createCell(0);
            cell.setCellValue(p.getProductDetailDTO().getProductName());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(p.getProductDetailDTO().getTypeName());

            cell = row.createCell(2);
            cell.setCellValue(p.getProductDetailDTO().getColorName());

            cell = row.createCell(3);
            cell.setCellValue(p.getQuantity());
            rowCount++;
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeOrderHeader();
        writeOrderDetail();
        writeProductHeader();
        writeProductDetail();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
