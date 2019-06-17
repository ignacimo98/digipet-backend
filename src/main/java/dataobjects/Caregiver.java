package dataobjects;

import java.sql.Date;
import java.util.List;

public class Caregiver {
    private int IdCaregiver;
    private String IdStudent;
    private int IdUniversity;
    private int IdProvince;
    private int IdCanton;
    private String Name;
    private String LastName;
    private String Email1;
    private String Email2;
    private String Photo;
    private Date InscriptionDate;
    private String PersonalDescription;
    private int WalksQuantity;
    private int Phone;
    private float WalksRating;
    private boolean WorksInOtherProvince;
    private String Password;
    private int Status;
    private List OtherProvincesId;
    private List Badges;

    public int getIdCaregiver() {
        return IdCaregiver;
    }

    public void setIdCaregiver(int idCaregiver) {
        IdCaregiver = idCaregiver;
    }

    public String getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(String idStudent) {
        IdStudent = idStudent;
    }

    public int getIdProvince() {
        return IdProvince;
    }

    public void setIdProvince(int idProvince) {
        IdProvince = idProvince;
    }

    public int getIdUniversity() {
        return IdUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        IdUniversity = idUniversity;
    }

    public int getIdCanton() {
        return IdCanton;
    }

    public void setIdCanton(int idCanton) {
        IdCanton = idCanton;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail1() {
        return Email1;
    }

    public void setEmail1(String email1) {
        Email1 = email1;
    }

    public String getEmail2() {
        return Email2;
    }

    public void setEmail2(String email2) {
        Email2 = email2;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Date getInscriptionDate() {
        return InscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        InscriptionDate = inscriptionDate;
    }

    public String getPersonalDescription() {
        return PersonalDescription;
    }

    public void setPersonalDescription(String personalDescription) {
        PersonalDescription = personalDescription;
    }

    public int getWalksQuantity() {
        return WalksQuantity;
    }

    public void setWalksQuantity(int walksQuantity) {
        WalksQuantity = walksQuantity;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }

    public float getWalksRating() {
        return WalksRating;
    }

    public void setWalksRating(float walksRating) {
        WalksRating = walksRating;
    }

    public boolean getWorksInOtherProvince() {
        return WorksInOtherProvince;
    }

    public void setWorksInOtherProvince(boolean worksInOtherProvince) {
        WorksInOtherProvince = worksInOtherProvince;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public List getOtherProvincesId() {
        return OtherProvincesId;
    }

    public void setOtherProvincesId(List otherProvincesId) {
        OtherProvincesId = otherProvincesId;
    }

    public List getBadges() {
        return Badges;
    }

    public void setBadges(List badges) {
        Badges = badges;
    }
}
