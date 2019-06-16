package dataobjects;

import java.sql.Date;
import java.util.List;

public interface Model {
    int createAdmin(String Username, String Email, String Password, Boolean Status);

    List getAllAdmins();
    List getAllPetsFromOwner(int IdPetOwner);

    String getClientIdType(String LoginData, String Password) throws Exception;
    String insertCaregiver(String IdStudent, int IdUniversity, int IdProvince, int IdCanton,
                           String Name, String LastName, String Email1, String Email2,
                           String Photo, String PersonalDescription, int Phone, boolean WorksInOtherProvince,
                           String Password, List<Integer> OtherProvincesId) throws Exception;

    String insertPetOwner(int IdProvince, int IdCanton, String Name, String LastName,
                          String Email1, String Email2, int Phone, String Photo,
                          String PersonalDescription, String Password) throws Exception;


    String insertPet(int IdPetOwner, String Name, int Age, String Size, String PetDescription, List<String> PhotoLinks);

    String insertService(int IdPet, int IdCaregiver, String StartTime, String EndTime, String OwnerComments,
                         String PickUpLocation);

    int assignCaregiver(int IdPet, int IdPetOwner, String StartTime, String EndTime, String Location);


    Administrator getAdminFromId(int IdAdministrator) throws Exception;
    PetOwner getPetOwnerFromId(int IdPetOwner) throws Exception;
    Pet getPetFromId(int IdPet) throws Exception;
    Caregiver getCaregiverFromId(int IdCaregiver) throws Exception;
    WalkService getServiceFromId(int IdWalkService) throws Exception;

    List getServicesForPetOwner(int IdPetOwner) throws Exception;
    List getServicesForPet(int IdPet) throws Exception;
    List getServicesForCaregiver(int IdCaregiver) throws Exception;

}