package dataobjects;

public class Configuration {
    private int IdConfiguration;
    private String Description;
    private int Value;

    public int getIdConfiguration() {
        return IdConfiguration;
    }

    public void setIdConfiguration(int idConfiguration) {
        IdConfiguration = idConfiguration;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }
}
