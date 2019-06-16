package dataobjects;

import java.sql.Date;
import java.util.List;

public interface Model {
    int createAdmin(String Username, String Email, String Password, Boolean Status);

    List getAllAdmins();
    List getAllPetsFromOwner(int IdPetOwner);
    List getPetFromId(int IdPet);
    String getClientIdType(String LoginData, String Password) throws Exception;
    String insertCaregiver(int IdStudent, int IdUniversity, int IdProvince, int IdCanton,
                           String Name, String LastName, String Email1, String Email2,
                           String Photo, String PersonalDescription, int Phone, boolean WorksInOtherProvince,
                           String Password, List<Integer> OtherProvincesId) throws Exception;

    String insertPetOwner(int IdProvince, int IdCanton, String Name, String LastName,
                          String Email1, String Email2, int Phone, String Photo,
                          String PersonalDescription, String Password) throws Exception;

    String insertPet(int IdPetOwner, String Name, int Age, String Size, String PetDescription, List<String> PhotoLinks);
}