package dataobjects;

import java.util.Date;

public class Pet {
    private int IdPet;
    private int IdPetOwner;
    private String Name;
    private int WalksQuantity;
    private Date InscriptionDate;
    private int Age;
    private String Size;
    private String PetDescription;
    private boolean Status;

    public int getIdPet() {
        return IdPet;
    }

    public void setIdPet(int idPet) {
        IdPet = idPet;
    }

    public int getIdPetOwner() {
        return IdPetOwner;
    }

    public void setIdPetOwner(int idPetOwner) {
        IdPetOwner = idPetOwner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getWalksQuantity() {
        return WalksQuantity;
    }

    public void setWalksQuantity(int walksQuantity) {
        WalksQuantity = walksQuantity;
    }

    public Date getInscriptionDate() {
        return InscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        InscriptionDate = inscriptionDate;
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

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
