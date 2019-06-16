package dataobjects;

public class Schedule {
    private int IdCaregiver;
    private String StartTime;
    private String EndTime;

    public int getIdCaregiver() {
        return IdCaregiver;
    }

    public void setIdCaregiver(int idCaregiver) {
        IdCaregiver = idCaregiver;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
