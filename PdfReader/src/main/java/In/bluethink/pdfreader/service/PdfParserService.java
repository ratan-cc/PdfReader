package In.bluethink.pdfreader.service;

import In.bluethink.pdfreader.entity.EmployeeShift;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class PdfParserService {

    public List<EmployeeShift> parse(String text) {

        List<EmployeeShift> list = new ArrayList<>();

        // Store number
        Matcher storeMatcher =
                Pattern.compile("Store\\s+#(\\d+)").matcher(text);
        String store = storeMatcher.find() ? storeMatcher.group(1) : "";

        // Employee block
        Pattern empPattern = Pattern.compile(
                "(\\d+)([A-Z ]+)\\s+HolDTOTRegID([\\s\\S]*?)(?=\\n\\d+[A-Z ]+ HolDTOTRegID|TOTAL HOURS)",
                Pattern.MULTILINE
        );

        Matcher matcher = empPattern.matcher(text);

        while (matcher.find()) {

            EmployeeShift e = new EmployeeShift();
            e.setStoreNumber(store);
            e.setName(matcher.group(2).trim());

            String block = matcher.group(3);

            // Business Day
            Matcher day = Pattern.compile(
                    "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)"
            ).matcher(block);
            if (day.find()) e.setBusinessDay(day.group());

            // Job ID (first numeric after Clock-out/Clock-in)
            Matcher jobMatcher = Pattern.compile(
                    "\\b(\\d{1,3})\\b"
            ).matcher(block);
            if (jobMatcher.find()) e.setJobId(jobMatcher.group(1));

            // In Type
            Matcher inTypeMatcher = Pattern.compile(
                    "(Clock-in|Break Clock-in|Break)"
            ).matcher(block);
            if (inTypeMatcher.find()) e.setInType(inTypeMatcher.group());

            // Out Type
            Matcher outTypeMatcher = Pattern.compile(
                    "(Clock-out|Break Clock-out)"
            ).matcher(block);
            if (outTypeMatcher.find()) e.setOutType(outTypeMatcher.group());

            // Clock In
            Matcher timeMatcher = Pattern.compile(
                    "(\\d{1,2}:\\d{2}(AM|PM))"
            ).matcher(block);

            if (timeMatcher.find()) e.setClockIn(timeMatcher.group());
            if (timeMatcher.find()) e.setClockOut(timeMatcher.group());

            list.add(e);
        }

        return list;
    }
}

