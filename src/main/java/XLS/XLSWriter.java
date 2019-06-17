package XLS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import dataobjects.ReportEntry;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class XLSWriter {
    public static void generateExcel(List<ReportEntry> Entries, String startDate, String endDate){
        try{
            String excelPath = "reportes/Reporte_"+startDate+"_"+endDate + ".xls";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(excelPath));

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Reporte Financiero");

            Row row = sheet.createRow(0);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Fecha de Transacción");

            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Ganancia");

            int rowNum = 1;
            int total = 0;

            for(ReportEntry Entry : Entries){
                row = sheet.createRow(rowNum++);

                cell0 = row.createCell(0);
                cell0.setCellValue(Entry.getEntryDate());

                cell1 = row.createCell(1);
                cell1.setCellValue(Entry.getPrice());

                total += Entry.getPrice();

            }

            row = sheet.createRow(rowNum+1);
            cell0 = row.createCell(0);
            cell0.setCellValue("Total Ganancias");

            cell1 = row.createCell(1);
            cell1.setCellValue(total);

            workbook.write(fileOutputStream);
            workbook.close();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
