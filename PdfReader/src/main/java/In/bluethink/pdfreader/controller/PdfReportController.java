package In.bluethink.pdfreader.controller;

import In.bluethink.pdfreader.entity.EmployeeShift;
import In.bluethink.pdfreader.service.ExcelService;
import In.bluethink.pdfreader.service.PdfParserService;
import In.bluethink.pdfreader.service.PdfReaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfReportController {

    private final PdfReaderService reader;
    private final PdfParserService parser;
    private final ExcelService excel;

    public PdfReportController(PdfReaderService reader,
                               PdfParserService parser,
                               ExcelService excel) {
        this.reader = reader;
        this.parser = parser;
        this.excel = excel;
    }

    @GetMapping("/generate-excel")
    public ResponseEntity<String> generateExcel() {
        try {
            String text = reader.readPdf();
            List<EmployeeShift> data = parser.parse(text);
            excel.generate(data);
            return ResponseEntity.ok("Excel generated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}
