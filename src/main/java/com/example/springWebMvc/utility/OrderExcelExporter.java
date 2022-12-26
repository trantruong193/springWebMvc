package com.example.springWebMvc.utility;

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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderExcelExporter {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private OrderDTO dto;
    private List<OrderDTO> list;
    public OrderExcelExporter(OrderDTO dto) {
        this.dto = dto;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Orders");
    }
    public OrderExcelExporter(List<OrderDTO> list) {
        this.list = list;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Orders");
    }

    private void writeOrderHeader(int index){
        Row row = sheet.createRow(index);
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
        cell.setCellValue("Total Price (include tax)");
        sheet.autoSizeColumn(0);
    }
    private void writeProductHeader(int index){
        Row row = sheet.createRow(index);
        // set font
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
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

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Unit Price");
    }
    private void writeOrderDetail(OrderDTO orderDTO,int index){
        Row row = sheet.createRow(index);

        Cell cell = row.createCell(0);
        cell.setCellValue(orderDTO.getOrderCode());
        sheet.autoSizeColumn(0);

        cell = row.createCell(1);
        cell.setCellValue(orderDTO.getCusName());
        sheet.autoSizeColumn(1);

        cell = row.createCell(2);
        cell.setCellValue(orderDTO.getPhone());
        sheet.autoSizeColumn(2);

        cell = row.createCell(3);
        cell.setCellValue(orderDTO.getAddress());
        sheet.autoSizeColumn(3);

        cell = row.createCell(4);
        cell.setCellValue(orderDTO.getEmail());
        sheet.autoSizeColumn(4);

        cell = row.createCell(5);
        cell.setCellValue(dateFormat.format(orderDTO.getCreateTime()));
        sheet.autoSizeColumn(5);

        cell = row.createCell(6);
        cell.setCellValue(Math.round((orderDTO.getTotalPrice()*100)/100)+" $");
    }
    private void writeProductDetail(OrderDTO orderDTO,int index){
        for (OrderDetailDTO p: orderDTO.getOrderDetailDTOList()){
            Row row = sheet.createRow(index);
            Cell cell = row.createCell(0);
            cell.setCellValue(p.getProductDetailDTO().getProductName());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(p.getProductDetailDTO().getTypeName());

            cell = row.createCell(2);
            cell.setCellValue(p.getProductDetailDTO().getColorName());

            cell = row.createCell(3);
            cell.setCellValue(p.getQuantity());

            cell = row.createCell(4);
            cell.setCellValue(Math.round((p.getPrice()*100)/100)+"$");
            index++;
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeOrderHeader(0);
        writeOrderDetail(dto,1);
        writeProductHeader(2);
        writeProductDetail(dto,3);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    public void exportAll(HttpServletResponse response) throws IOException {
        int index = 0;
        for (OrderDTO order : list){
            writeOrderHeader(index);
            index++;
            writeOrderDetail(order,index);
            index++;
            writeProductHeader(index);
            index++;
            writeProductDetail(order,index);
            index+=order.getOrderDetailDTOList().size();
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
