package In.bluethink.pdfreader;


import In.bluethink.pdfreader.entity.EmployeeShift;
import In.bluethink.pdfreader.service.ExcelService;
import In.bluethink.pdfreader.service.PdfParserService;
import In.bluethink.pdfreader.service.PdfReaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class pdfReaderApplication implements CommandLineRunner {

    private final PdfReaderService reader;
    private final PdfParserService parser;
    private final ExcelService excel;

    public pdfReaderApplication(PdfReaderService reader,
                     PdfParserService parser,
                     ExcelService excel) {
        this.reader = reader;
        this.parser = parser;
        this.excel = excel;
    }


    public static void main(String[] args) {
        SpringApplication.run(pdfReaderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            String text = reader.readPdf();

            System.out.println("========== RAW PDF TEXT START ==========");
            System.out.println(text);
            System.out.println("========== RAW PDF TEXT END ==========");

            List<EmployeeShift> data = parser.parse(text);
            excel.generate(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

