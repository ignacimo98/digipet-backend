package dataobjects;

public class Province {

    private int IdProvince;
    private int IdCountry;
    private String Name;
    private boolean Status;

    public int getIdProvince() {
        return IdProvince;
    }

    public void setIdProvince(int IdProvince) {
        this.IdProvince = IdProvince;
    }

    public int getIdCountry() {
        return IdCountry;
    }

    public void setIdCountry(int idCountry) {
        IdCountry = idCountry;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
