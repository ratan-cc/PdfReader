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

        List<EmployeeShift> result = new ArrayList<>();

        // Store number
        Matcher storeMatcher =
                Pattern.compile("Store\\s+#(\\d+)").matcher(text);
        String store = storeMatcher.find() ? storeMatcher.group(1) : "";

        // Employee blocks
        Pattern empPattern = Pattern.compile(
                "(\\d+)([A-Z ]+)\\s+HolDTOTRegID([\\s\\S]*?)(?=\\n\\d+[A-Z ]+ HolDTOTRegID|TOTAL HOURS)",
                Pattern.MULTILINE
        );

        Matcher empMatcher = empPattern.matcher(text);

        while (empMatcher.find()) {

            String employeeName = empMatcher.group(2).trim();
            String block = empMatcher.group(3);

            // Business day
            Matcher dayMatcher = Pattern.compile(
                    "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)"
            ).matcher(block);
            String businessDay = dayMatcher.find() ? dayMatcher.group() : "";

            // CLOCK ROW pattern (THIS IS THE FIX)
            Pattern clockRowPattern = Pattern.compile(
                    "(Clock-in|Break Clock-in)\\s+" +
                            "(Break|Clock-out)\\s+" +
                            "(\\d+)\\s+" +
                            ".*?(\\d{1,2}:\\d{2}(AM|PM)).*?" +
                            "(\\d{1,2}:\\d{2}(AM|PM))"
            );

            Matcher clockMatcher = clockRowPattern.matcher(block);

            while (clockMatcher.find()) {

                EmployeeShift entry = new EmployeeShift();

                entry.setStoreNumber(store);
                entry.setName(employeeName);
                entry.setBusinessDay(businessDay);

                entry.setInType(clockMatcher.group(1));
                entry.setOutType(clockMatcher.group(2));
                entry.setJobId(clockMatcher.group(3));
                entry.setClockIn(clockMatcher.group(4));
                entry.setClockOut(clockMatcher.group(6));

                result.add(entry);
            }
        }

        return result;
    }
}
