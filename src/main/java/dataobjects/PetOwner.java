package dataobjects;

import java.sql.Date;

public class PetOwner {
    private int IdPetOwner;
    private int IdProvince;
    private int IdCanton;
    private String Name;
    private String LastName;
    private String Email1;
    private String Email2;
    private int Phone;
    private String Photo;
    private Date InscriptionDate;
    private String PersonalDescription;
    private String Password;
    private boolean Status;

    public int getIdPetOwner() {
        return IdPetOwner;
    }

    public void setIdPetOwner(int idPetOwner) {
        IdPetOwner = idPetOwner;
    }

    public int getIdProvince() {
        return IdProvince;
    }

    public void setIdProvince(int idProvince) {
        IdProvince = idProvince;
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

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
