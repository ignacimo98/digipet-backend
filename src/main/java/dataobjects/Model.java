package dataobjects;

import java.util.List;

public interface Model {
    int createAdmin(String Username, String Email, String Password, Boolean Status);

    List getAllAdmins();
    List getAllPetsFromOwner(int IdPetOwner);
    List getPetFromId(int IdPet);
    String getClientIdType(String LoginData, String Password) throws Exception;
}