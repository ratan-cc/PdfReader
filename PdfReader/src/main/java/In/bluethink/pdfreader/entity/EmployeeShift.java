package In.bluethink.pdfreader.entity;

public class EmployeeShift {
    private String storeNumber;
    private String name;
    private String businessDay;
    private String clockIn;
    private String clockOut;
    private String inType;
    private String outType;
    private String jobId;

    // Getters and Setters
    public String getStoreNumber() { return storeNumber; }
    public void setStoreNumber(String storeNumber) { this.storeNumber = storeNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBusinessDay() { return businessDay; }
    public void setBusinessDay(String businessDay) { this.businessDay = businessDay; }

    public String getClockIn() { return clockIn; }
    public void setClockIn(String clockIn) { this.clockIn = clockIn; }

    public String getClockOut() { return clockOut; }
    public void setClockOut(String clockOut) { this.clockOut = clockOut; }

    public String getInType() { return inType; }
    public void setInType(String inType) { this.inType = inType; }

    public String getOutType() { return outType; }
    public void setOutType(String outType) { this.outType = outType; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
}

