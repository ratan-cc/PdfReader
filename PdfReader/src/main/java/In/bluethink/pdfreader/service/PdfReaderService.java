package In.bluethink.pdfreader.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfReaderService {

    public String readPdf() throws IOException {

        ClassPathResource resource =
                new ClassPathResource("reports/Detail-Sign-Off-Report.pdf");

        if (!resource.exists()) {
            throw new FileNotFoundException(
                    "PDF not found in classpath: reports/Detail-Sign-Off-Report.pdf"
            );
        }

        try (InputStream is = resource.getInputStream();
             PDDocument document = PDDocument.load(is)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
