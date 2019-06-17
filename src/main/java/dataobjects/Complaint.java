package dataobjects;

public class Complaint {
    private int IdWalkService;
    private String Description;
    private boolean Status;

    public int getIdWalkService() {
        return IdWalkService;
    }

    public void setIdWalkService(int idWalkService) {
        IdWalkService = idWalkService;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
