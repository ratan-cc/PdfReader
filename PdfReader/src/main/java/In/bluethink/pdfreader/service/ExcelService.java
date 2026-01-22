package In.bluethink.pdfreader.service;

import In.bluethink.pdfreader.entity.EmployeeShift;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import java.io.File;


@Service
public class ExcelService {

    @Value("${output.excel.path}")
    private String outputPath;

    public void generate(List<EmployeeShift> data) throws Exception {

        // Ensure output directory exists
        File file = new File(outputPath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SignOff");

        Row header = sheet.createRow(0);
        String[] headers = {
                "Store",
                "Employee Name",
                "Business Day",
                "In Type",
                "Out Type",
                "Clock In",
                "Clock Out",
                "Job ID"
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
            row.createCell(3).setCellValue(e.getInType());
            row.createCell(4).setCellValue(e.getOutType());
            row.createCell(5).setCellValue(e.getClockIn());
            row.createCell(6).setCellValue(e.getClockOut());
            row.createCell(7).setCellValue(e.getJobId());
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }

        workbook.close();

        System.out.println("Excel generated at: " + file.getAbsolutePath());
    }
}
