package In.bluethink.pdfreader.service;

import In.bluethink.pdfreader.entity.EmployeeShift;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;
@Service
public class ExcelService {

    @Value("${output.excel.path}")
    private String outputPath;

    public void generate(List<EmployeeShift> data) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SignOff");

        Row header = sheet.createRow(0);
        String[] headers = {
                "Store", "Name", "Business Day",
                "Clock In", "Clock Out", "Job ID"
        };

        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (EmployeeShift e : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getStoreNumber());
            row.createCell(1).setCellValue(e.getName());
            row.createCell(2).setCellValue(e.getBusinessDay());
            row.createCell(3).setCellValue(e.getClockIn());
            row.createCell(4).setCellValue(e.getClockOut());
            row.createCell(5).setCellValue(e.getInType());
            row.createCell(6).setCellValue(e.getOutType());
            row.createCell(7).setCellValue(e.getJobId());

        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }

        workbook.close();
        System.out.println("Excel generated at: " + outputPath);
    }

    public void generate(List<EmployeeShift> data, String s) {
    }

}
