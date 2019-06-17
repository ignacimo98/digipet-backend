package dataobjects;

public class ReportEntry {
    private String EntryDate;
    private String ClientName;
    private String ClientLastName;
    private String PetName;
    private String Province;
    private String Canton;
    private int TotalPrice;
    private int UnitPrice;
    private String StudentName;
    private String StudentLastName;
    private String ServiceType;


    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientLastName() {
        return ClientLastName;
    }

    public void setClientLastName(String clientLastName) {
        ClientLastName = clientLastName;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCanton() {
        return Canton;
    }

    public void setCanton(String canton) {
        Canton = canton;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentLastName() {
        return StudentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        StudentLastName = studentLastName;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }
}
