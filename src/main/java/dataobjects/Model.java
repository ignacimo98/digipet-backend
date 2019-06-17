package dataobjects;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Date;
import java.util.List;

public interface Model {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Login
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    String getClientIdType(String LoginData, String Password) throws Exception;

    String insertCaregiver(String IdStudent, int IdUniversity, int IdProvince, int IdCanton,
                           String Name, String LastName, String Email1, String Email2,
                           String Photo, String PersonalDescription, int Phone, boolean WorksInOtherProvince,
                           String Password, List<Integer> OtherProvincesId) throws Exception;

    String insertPetOwner(int IdProvince, int IdCanton, String Name, String LastName,
                          String Email1, String Email2, int Phone, String Photo,
                          String PersonalDescription, String Password) throws Exception;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Administrator
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int createAdmin(String Username, String Email, String Password, Boolean Status);
    List getAllAdmins();
    Administrator getAdminFromId(int IdAdministrator) throws Exception;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Clients
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    List getAllPetsFromOwner(int IdPetOwner);
    PetOwner getPetOwnerFromId(int IdPetOwner) throws Exception;
    List getServicesForPetOwner(int IdPetOwner) throws Exception;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Pets
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    String insertPet(int IdPetOwner, String Name, int Age, String Size, String PetDescription, List<String> PhotoLinks);
    String disablePet(int IdPet) throws Exception;
    Pet getPetFromId(int IdPet) throws Exception;
    List getServicesForPet(int IdPet) throws Exception;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Caregivers
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    Caregiver getCaregiverFromId(int IdCaregiver) throws Exception;
    List getServicesForCaregiver(int IdCaregiver) throws Exception;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Services
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    List getComplaints();
    WalkService getServiceFromId(int IdWalkService) throws Exception;
    String getServicePrice(String StartTime, String EndTime);
    int assignCaregiver(int IdPet, int IdPetOwner, String StartTime, String EndTime, String Location);
    String blockCaregiver(int idCaregiver) throws Exception;
    String insertService(int IdPet, int IdCaregiver, String StartTime, String EndTime, String OwnerComments,
                         String PickUpLocation);
    String insertComplaint(int IdService, String Description) throws Exception;
    String updateReport(int IdService, String ReportDescription) throws Exception;
    ObjectNode updateRate(int IdService, int Rate) throws Exception;
    List getReport(String StartDate, String EndDate);


    public String insertHours(int idCaregiver, String startTime, String endTime) throws Exception;




}