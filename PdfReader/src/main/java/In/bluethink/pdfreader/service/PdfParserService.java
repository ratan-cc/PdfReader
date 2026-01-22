package In.bluethink.pdfreader.service;

import In.bluethink.pdfreader.entity.EmployeeShift;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;
@Service
public class PdfParserService {

    public List<EmployeeShift> parse(String rawText) {

        List<EmployeeShift> result = new ArrayList<>();

        // -------------------------------
        // 1. NORMALIZE PDF TEXT
        // -------------------------------
        String text = rawText
                .replace("\r", "")
                .replaceAll("\\n+", "\n")
                .replace("*Clock-in", "Clock-in")
                .replaceAll("Break\\s+Clock-in", "Break Clock-in")
                .replaceAll("Break\\s+Clock-out", "Break Clock-out");

        String[] lines = text.split("\n");

        // -------------------------------
        // 2. GLOBAL STORE NUMBER
        // -------------------------------
        String storeNumber = "";
        Matcher storeMatcher = Pattern.compile("Store\\s+#(\\d+)").matcher(text);
        if (storeMatcher.find()) {
            storeNumber = storeMatcher.group(1);
        }

        // -------------------------------
        // 3. PATTERNS
        // -------------------------------
        Pattern employeePattern = Pattern.compile("^(\\d+)([A-Z ]+)\\s+HolDTOTRegID");
        Pattern dayPattern = Pattern.compile("(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)");
        Pattern timePattern = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{2}\\s+\\d{1,2}:\\d{2}(AM|PM))");
        Pattern jobCodePattern = Pattern.compile("(Clock-in|Break Clock-in).*?(\\d+)");

        // -------------------------------
        // 4. STATE VARIABLES
        // -------------------------------
        String employeeName = null;
        String businessDay = null;
        String jobCode = null;

        String lastInType = null;
        String lastInTime = null;

        // -------------------------------
        // 5. LINE-BY-LINE PARSING
        // -------------------------------
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // EMPLOYEE HEADER
            Matcher empMatcher = employeePattern.matcher(line);
            if (empMatcher.find()) {
                employeeName = empMatcher.group(2).trim();
                continue;
            }

            // BUSINESS DAY
            Matcher dayMatcher = dayPattern.matcher(line);
            if (dayMatcher.find()) {
                businessDay = dayMatcher.group(1);
            }

            // JOB CODE
            Matcher jobMatcher = jobCodePattern.matcher(line);
            if (jobMatcher.find()) {
                jobCode = jobMatcher.group(2);
            }

            // -------------------------------
            // CLOCK-IN / BREAK CLOCK-IN
            // -------------------------------
            if (line.contains("Clock-in")) {
                Matcher timeMatcher = timePattern.matcher(line);
                if (timeMatcher.find()) {
                    lastInType = line.contains("Break") ? "Break Clock-in" : "Clock-in";
                    lastInTime = timeMatcher.group(1);
                }
            }

            // -------------------------------
            // CLOCK-OUT / BREAK CLOCK-OUT
            // -------------------------------
            if (line.contains("Clock-out")) {
                Matcher timeMatcher = timePattern.matcher(line);
                if (timeMatcher.find() && lastInTime != null) {

                    String outType = line.contains("Break") ? "Break Clock-out" : "Clock-out";
                    String outTime = timeMatcher.group(1);

                    EmployeeShift row = new EmployeeShift();
                    row.setStoreNumber(storeNumber);
                    row.setName(employeeName);
                    row.setBusinessDay(businessDay);
                    row.setJobId(jobCode);
                    row.setInType(lastInType);
                    row.setOutType(outType);
                    row.setClockIn(lastInTime);
                    row.setClockOut(outTime);

                    result.add(row);

                    // RESET AFTER PAIR
                    lastInType = null;
                    lastInTime = null;
                }
            }
        }

        System.out.println("Parsed rows: " + result.size());
        return result;
    }
}
