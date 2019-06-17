package dataobjects;

public class Canton {
    private int IdCanton;
    private int IdProvince;
    private String Name;
    private boolean Status;

    public int getIdCanton() {
        return IdCanton;
    }

    public void setIdCanton(int idCanton) {
        IdCanton = idCanton;
    }

    public int getIdProvince() {
        return IdProvince;
    }

    public void setIdProvince(int idProvince) {
        IdProvince = idProvince;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
