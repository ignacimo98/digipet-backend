package dataobjects;

public class WalkService {
    private int IdWalkService;
    private int IdPet;
    private int IdCaregiver;
    private String StartTime;
    private String EndTime;
    private int Price;
    private String OwnerComments;
    private String PickUpLocation;
    private String ReportDescription;
    private boolean Status;
    private int Rating;

    public int getIdWalkService() {
        return IdWalkService;
    }

    public void setIdWalkService(int idWalkService) {
        IdWalkService = idWalkService;
    }

    public int getIdPet() {
        return IdPet;
    }

    public void setIdPet(int idPet) {
        IdPet = idPet;
    }

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

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getOwnerComments() {
        return OwnerComments;
    }

    public void setOwnerComments(String ownerComments) {
        OwnerComments = ownerComments;
    }

    public String getPickUpLocation() {
        return PickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        PickUpLocation = pickUpLocation;
    }

    public String getReportDescription() {
        return ReportDescription;
    }

    public void setReportDescription(String reportDescription) {
        ReportDescription = reportDescription;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }
}
