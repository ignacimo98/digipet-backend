package dataobjects;

public class University {
    private int IdUniversity;
    private int IdCountry;
    private String Name;
    private boolean Status;

    public int getIdUniversity() {
        return IdUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        IdUniversity = idUniversity;
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

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
