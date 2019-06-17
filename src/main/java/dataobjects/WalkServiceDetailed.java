package dataobjects;

import java.util.Date;
import java.util.List;

public class WalkServiceDetailed {

    private String StudentName;
    private String StudentLastName;
    private String EmailStudent;
    private String StudentPhoto;
    private int WalksQuantity;
    private float WalksRating;
    private int PhoneStudent;

    private String ClientName;
    private String ClientLastName;
    private String EmailClient;
    private int ClientPhone;
    private String ClientPhoto;

    private int IdPet;
    private String PetName;
    private int Age;
    private String Size;
    private String PetDescription;
    private List PhotoLinks;

    private String StartTime;
    private String EndTime;
    private int Price;
    private String OwnerComments;
    private String PickUpLocation;
    private String ReportDescription;
    private boolean WalkServiceStatus;
    private int Rating;

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

    public String getEmailStudent() {
        return EmailStudent;
    }

    public void setEmailStudent(String emailStudent) {
        EmailStudent = emailStudent;
    }

    public String getStudentPhoto() {
        return StudentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        StudentPhoto = studentPhoto;
    }

    public int getWalksQuantity() {
        return WalksQuantity;
    }

    public void setWalksQuantity(int walksQuantity) {
        WalksQuantity = walksQuantity;
    }

    public int getPhoneStudent() {
        return PhoneStudent;
    }

    public void setPhoneStudent(int phoneStudent) {
        PhoneStudent = phoneStudent;
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

    public String getEmailClient() {
        return EmailClient;
    }

    public void setEmailClient(String emailClient) {
        EmailClient = emailClient;
    }

    public int getClientPhone() {
        return ClientPhone;
    }

    public void setClientPhone(int clientPhone) {
        ClientPhone = clientPhone;
    }

    public String getClientPhoto() {
        return ClientPhoto;
    }

    public void setClientPhoto(String clientPhoto) {
        ClientPhoto = clientPhoto;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getPetDescription() {
        return PetDescription;
    }

    public void setPetDescription(String petDescription) {
        PetDescription = petDescription;
    }

    public List getPhotoLinks() {
        return PhotoLinks;
    }

    public void setPhotoLinks(List photoLinks) {
        PhotoLinks = photoLinks;
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

    public boolean isWalkServiceStatus() {
        return WalkServiceStatus;
    }

    public void setWalkServiceStatus(boolean walkServiceStatus) {
        WalkServiceStatus = walkServiceStatus;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public float getWalksRating() {
        return WalksRating;
    }

    public void setWalksRating(float walksRating) {
        WalksRating = walksRating;
    }

    public int getIdPet() {
        return IdPet;
    }

    public void setIdPet(int idPet) {
        IdPet = idPet;
    }
}
