package sql2omodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dataobjects.*;
import org.mindrot.jbcrypt.BCrypt;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import routing.Token;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.*;


public class Sql2oModel implements Model {
    private Sql2o sql2o;

    public boolean timeForNewCaregiver;

    public Sql2oModel(Sql2o sql2o){
        this.sql2o = sql2o;
        this.timeForNewCaregiver = false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Login
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String insertCaregiver(String IdStudent, int IdUniversity, int IdProvince, int IdCanton, String Name,
                                  String LastName, String Email1, String Email2, String Photo, String PersonalDescription,
                                  int Phone, boolean WorksInOtherProvince, String Password, List<Integer> OtherProvincesId)
            throws Exception{


        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdCaregiver) FROM Caregiver WHERE IdStudent = :IdStudent");
        query.addParameter("IdStudent", IdStudent);
        int existsCaregiver = query.executeScalar(Integer.class);

        if(existsCaregiver == 0){
            query = connection.createQuery("INSERT INTO Caregiver(IdStudent, IdUniversity, IdProvince, IdCanton, Name," +
                    "LastName, Email1, Email2, Photo, InscriptionDate, PersonalDescription, Phone, WorksInOtherProvince, Password) " +
                    "VALUES (:IdStudent, :IdUniversity, :IdProvince, :IdCanton, :Name, :LastName, :Email1, :Email2, :Photo," +
                    ":InscriptionDate, :PersonalDescription, :Phone, :WorksInOtherProvince, :Password)");

            query.addParameter("IdStudent", IdStudent);
            query.addParameter("IdUniversity", IdUniversity);
            query.addParameter("IdProvince", IdProvince);
            query.addParameter("IdCanton", IdCanton);
            query.addParameter("Name", Name);
            query.addParameter("LastName", LastName);
            query.addParameter("Email1", Email1);
            query.addParameter("Email2", Email2);
            query.addParameter("Photo", Photo);
            java.sql.Date InscriptionDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            query.addParameter("InscriptionDate",InscriptionDate);
            query.addParameter("PersonalDescription", PersonalDescription);
            query.addParameter("Phone", Phone);
            query.addParameter("WorksInOtherProvince", WorksInOtherProvince);
            query.addParameter("Password", BCrypt.hashpw(Password, BCrypt.gensalt()));
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            query.executeUpdate();
            connection.commit();

            int idCaregiver = connection.createQuery("SELECT IdCaregiver FROM Caregiver WHERE IdStudent = :IdStudent")
                    .addParameter("IdStudent", IdStudent).executeScalar(Integer.class);


            if(WorksInOtherProvince) {
                for (int id : OtherProvincesId) {
                    query = connection.createQuery("INSERT INTO ProvinceXCaregiver(IdCaregiver, IdProvince) " +
                            "VALUES(:IdCaregiver, :IdProvince)");
                    query.addParameter("IdCaregiver", idCaregiver);
                    query.addParameter("IdProvince", id);
                    query.executeUpdate();
                }
            }

            int IdCaregiver = connection.createQuery("SELECT IdCaregiver FROM Caregiver WHERE IdStudent = :IdStudent")
                    .addParameter("IdStudent", IdStudent).executeScalar(Integer.class);

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("id", IdCaregiver);
            objectNode.put("type", "Student");
            objectNode.put("token", Token.generateToken(IdCaregiver, "Student"));

            return objectNode.toString();
        }
        else{
            throw new Exception("Ya existe un usuario con este carné. Por favor intente con otro.");

        }
    }

    @Override
    public String insertPetOwner(int IdProvince, int IdCanton, String Name, String LastName, String Email1,
                                 String Email2, int Phone, String Photo, String PersonalDescription,
                                 String Password) throws Exception{

        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdPetOwner) FROM PetOwner WHERE Email1 = :Email1");
        query.addParameter("Email1", Email1);
        int existsPetOwner = query.executeScalar(Integer.class);

        if(existsPetOwner == 0) {

            query = connection.createQuery("INSERT INTO PetOwner(IdProvince, IdCanton, Name, LastName, " +
                    "Email1, Email2, Phone, Photo, InscriptionDate, PersonalDescription, Password) VALUES (:IdProvince, " +
                    ":IdCanton, :Name, :LastName, :Email1, :Email2, :Phone, :Photo, :InscriptionDate, :PersonalDescription, :Password)");

            query.addParameter("IdProvince", IdProvince);
            query.addParameter("IdCanton", IdCanton);
            query.addParameter("Name", Name);
            query.addParameter("LastName", LastName);
            query.addParameter("Email1", Email1);
            query.addParameter("Email2", Email2);
            query.addParameter("Phone", Phone);
            query.addParameter("Photo", Photo);
            java.sql.Date InscriptionDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            query.addParameter("InscriptionDate",InscriptionDate);
            query.addParameter("PersonalDescription", PersonalDescription);
            query.addParameter("Password", BCrypt.hashpw(Password, BCrypt.gensalt()));
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            query.executeUpdate();
            connection.commit();

            int IdPetOwner = connection.createQuery("SELECT IdPetOwner FROM PetOwner WHERE Email1 = :Email1")
                    .addParameter("Email1", Email1).executeScalar(Integer.class);

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("id", IdPetOwner);
            objectNode.put("type", "Client");
            objectNode.put("token", Token.generateToken(IdPetOwner, "Client"));

            return objectNode.toString();
        }
        else{
            throw new Exception("Ya existe un usuario con este email. Por favor intente con otro.");
        }

    }
















    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Administrator
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public int createAdmin(String Username, String Email, String Password, Boolean Status) {
        try (Connection connection = sql2o.beginTransaction()){
            connection.createQuery("INSERT INTO Administrator(Username, Email, Password, Status) " +
                    "VALUES (:Username, :Email, :Password, :Status)")
                    .addParameter("Username", Username)
                    .addParameter("Email", Email)
                    .addParameter("Password", BCrypt.hashpw(Password, BCrypt.gensalt()))
                    .addParameter("Status", Status)
                    .executeUpdate();
            connection.commit();
        }
        return 0;
    }

    @Override
    public List<Administrator> getAllAdmins() {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT * FROM Administrator");
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            return query.executeAndFetch(Administrator.class);
        }
        catch (Exception e){
            System.out.println("Error");
            throw e;
        }
    }

    @Override
    public Administrator getAdminFromId(int IdAdministrator) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Administrator WHERE IdAdministrator = :IdAdministrator");
        query.addParameter("IdAdministrator", IdAdministrator);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<Administrator> administrator = query.executeAndFetch(Administrator.class);

        if(!administrator.isEmpty()){
            return administrator.get(0);
        }
        else{
            throw new Exception("Administrador no encontrado.");
        }
    }

    @Override
    public String setPrice(int Price) {
        try {
            Connection connection = sql2o.open();
            Query query = connection.createQuery("UPDATE Configuration SET Value = :Price WHERE Description = 'Precio'");
            query.addParameter("Price", Price);
            query.executeUpdate();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            return objectNode.toString();

        }
        catch (Exception e){
            e.printStackTrace();
            return "Error en update";
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Clients
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public PetOwner getPetOwnerFromId(int IdPetOwner) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM PetOwner WHERE IdPetOwner = :IdPetOwner");
        query.addParameter("IdPetOwner", IdPetOwner);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<PetOwner> petOwner = query.executeAndFetch(PetOwner.class);

        if(!petOwner.isEmpty()){
            return petOwner.get(0);
        }
        else{
            throw new Exception("Cliente no encontrado.");
        }
    }

    @Override
    public List getAllPetsFromOwner(int IdPetOwner) {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT * FROM Pet WHERE IdPetOwner = :IdPetOwner " +
                    "AND Status != 0");
            query.addParameter("IdPetOwner", IdPetOwner);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            List<Pet> pets = query.executeAndFetch(Pet.class);

            for(Pet pet : pets){
                query = connection.createQuery("SELECT Link FROM PetPhoto WHERE IdPet = :IdPet")
                .addParameter("IdPet", pet.getIdPet());
                pet.setPhotoLinks(query.executeScalarList(String.class));
            }

            return pets;
        }
        catch (Exception e){
            System.out.println("Error");
            throw e;
        }
    }

    @Override
    public List getServicesForPetOwner(int IdPetOwner) throws Exception{
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT W.IdWalkService, W.IdPet, W.IdCaregiver, W.StartTime, W.EndTime," +
                " W.Price, W.OwnerComments, W.PickUpLocation, W.ReportDescription, W.Status, Rating FROM WalkService W\n" +
                "INNER JOIN Pet P on W.IdPet = P.IdPet\n" +
                "INNER JOIN PetOwner PO on P.IdPetOwner = PO.IdPetOwner\n" +
                "WHERE PO.IdPetOwner = :IdPetOwner");

        query.addParameter("IdPetOwner", IdPetOwner);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<WalkService> walkServices = query.executeAndFetch(WalkService.class);

        if(!walkServices.isEmpty()){
            return walkServices;
        }
        else{
            throw new Exception("Servicios no disponibles para este cliente.");
        }
    }

    @Override
    public String changePetOwnerStatus(int IdPetOwner, boolean Status) throws Exception{
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdPetOwner) FROM Pet WHERE IdPetOwner = :IdPetOwner");
        query.addParameter("IdPetOwner", IdPetOwner);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        int existsPetOwner = query.executeScalar(Integer.class);

        if(existsPetOwner >= 1) {

            query = connection.createQuery("UPDATE PetOwner SET Status = :Status " +
                    "WHERE IdPetOwner = :IdPetOwner");
            query.addParameter("IdPetOwner", IdPetOwner);
            query.addParameter("Status", Status);
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            return objectNode.toString();
        }
        else{
            throw new Exception("No se ha encontrado el cliente.");
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Pets
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Pet getPetFromId(int IdPet) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Pet WHERE IdPet = :IdPet");
        query.addParameter("IdPet", IdPet);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<Pet> pet = query.executeAndFetch(Pet.class);

        if(!pet.isEmpty()){
            Pet result = pet.get(0);
            query = connection.createQuery("SELECT Link FROM PetPhoto WHERE IdPet = :IdPet");
            query.addParameter("IdPet", IdPet);
            result.setPhotoLinks(query.executeScalarList(String.class));
            return result;
        }
        else{
            throw new Exception("Mascota no encontrada.");
        }
    }

    @Override
    public List getServicesForPet(int IdPet) throws Exception{
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM WalkService WHERE IdPet = :IdPet");
        query.addParameter("IdPet", IdPet);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<WalkService> walkServices = query.executeAndFetch(WalkService.class);

        if(!walkServices.isEmpty()){
            return walkServices;
        }
        else{
            throw new Exception("Servicios no disponibles para esta mascota.");
        }
    }

    @Override
    public String insertPet(int IdPetOwner, String Name, int Age, String Size, String PetDescription, List<String> PhotoLinks) {
        try {
            Connection connection = sql2o.beginTransaction();
            Query query = connection.createQuery("INSERT INTO Pet(IdPetOwner, Name, Age, " +
                    "Size, PetDescription, InscriptionDate) VALUES (:IdPetOwner, :Name, :Age, :Size, :PetDescription, " +
                    ":InscriptionDate)");

            query.addParameter("IdPetOwner", IdPetOwner);
            query.addParameter("Name", Name);
            query.addParameter("Age", Age);
            query.addParameter("Size", Size);
            query.addParameter("PetDescription", PetDescription);
            java.sql.Date InscriptionDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            query.addParameter("InscriptionDate", InscriptionDate);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            query.executeUpdate();
            connection.commit();

            int idPet = connection.createQuery("SELECT IdPet FROM Pet WHERE Name = :Name")
                    .addParameter("Name", Name).executeScalar(Integer.class);

            for (String link : PhotoLinks) {
                query = connection.createQuery("INSERT INTO PetPhoto(IdPet, Link) " +
                        "VALUES(:IdPet, :Link)");
                query.addParameter("IdPet", idPet);
                query.addParameter("Link", link);
                query.executeUpdate();
            }

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.toString());
            return objectNode.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String disablePet(int IdPet) throws Exception{
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdPet) FROM Pet WHERE IdPet = :IdPet");
        query.addParameter("IdPet", IdPet);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        int existsPet = query.executeScalar(Integer.class);

        if(existsPet == 1) {

            query = connection.createQuery("UPDATE Pet SET Status = 0 " +
                    "WHERE IdPet = :IdPet");
            query.addParameter("IdPet", IdPet);
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            return objectNode.toString();
        }
        else{
            throw new Exception("No se ha encontrado la mascota.");
        }
    }

















    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Caregiver
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Caregiver getCaregiverFromId(int IdCaregiver) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Caregiver WHERE IdCaregiver = :IdCaregiver");
        query.addParameter("IdCaregiver", IdCaregiver);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<Caregiver> caregiver = query.executeAndFetch(Caregiver.class);

        if(!caregiver.isEmpty()){
            Caregiver result = caregiver.get(0);
            query = connection.createQuery("SELECT IdProvince FROM ProvinceXCaregiver WHERE IdCaregiver = :IdCaregiver");
            query.addParameter("IdCaregiver", IdCaregiver);
            result.setOtherProvincesId(query.executeScalarList(Integer.class));
            query = connection.createQuery("SELECT BadgeType FROM Badge WHERE IdCaregiver = :IdCaregiver");
            query.addParameter("IdCaregiver", IdCaregiver);
            result.setBadges(query.executeScalarList(Integer.class));
            return result;
        }
        else{
            throw new Exception("Estudiante no encontrado.");
        }
    }

    @Override
    public List getServicesForCaregiver(int IdCaregiver) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM WalkService WHERE IdCaregiver = :IdCaregiver");
        query.addParameter("IdCaregiver", IdCaregiver);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<WalkService> walkServices = query.executeAndFetch(WalkService.class);

        if(!walkServices.isEmpty()){
            return walkServices;
        }
        else{
            throw new Exception("Servicios no disponibles para este cuidador.");
        }
    }

    @Override
    public List getAllCaregivers() throws Exception {
        try {
            Connection connection = sql2o.open();
            Query query = connection.createQuery("SELECT * FROM Caregiver");
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

            List<Caregiver> caregivers = query.executeAndFetch(Caregiver.class);

            for (Caregiver caregiver : caregivers) {
                query = connection.createQuery("SELECT IdProvince FROM ProvinceXCaregiver WHERE IdCaregiver = :IdCaregiver")
                        .addParameter("IdCaregiver", caregiver.getIdCaregiver());
                caregiver.setOtherProvincesId(query.executeScalarList(Integer.class));

                query = connection.createQuery("SELECT BadgeType FROM Badge WHERE IdCaregiver = :IdCaregiver")
                        .addParameter("IdCaregiver", caregiver.getIdCaregiver());
                caregiver.setBadges(query.executeScalarList(Integer.class));

            }
            return caregivers;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    @Override
    public String changeCaregiverStatus(int IdCaregiver, int Status) throws Exception {
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdCaregiver) FROM Caregiver WHERE IdCaregiver = :IdCaregiver");
        query.addParameter("IdCaregiver", IdCaregiver);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        int existsCaregiver = query.executeScalar(Integer.class);

        int isBlocked = connection.createQuery("SELECT Status FROM Caregiver WHERE IdCaregiver = :IdCaregiver")
            .addParameter("IdCaregiver", IdCaregiver).executeScalar(Integer.class);

        if(existsCaregiver == 1 && isBlocked != 2) {

            query = connection.createQuery("UPDATE Caregiver SET Status = :Status " +
                    "WHERE IdCaregiver = :IdCaregiver");
            query.addParameter("IdCaregiver", IdCaregiver);
            query.addParameter("Status", Status);
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            return objectNode.toString();
        }
        else{
            throw new Exception("No se puede cambiar el estado del cuidador");
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////                                         Services
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    public List getComplaints() {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT * FROM Complaint WHERE Status = 0");
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            return query.executeAndFetch(Complaint.class);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public List getReport(String StartDate, String EndDate) {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT DATE(StartTime) AS EntryDate, PO.Name AS ClientName, PO.LastName AS ClientLastName, C.Name AS StudentName,\n" +
                    "       C.LastName AS StudentLastName, P2.Name AS Province, C2.Name AS Canton, Price AS TotalPrice, P.Name AS PetName FROM WalkService\n" +
                    "INNER JOIN Caregiver C on WalkService.IdCaregiver = C.IdCaregiver\n" +
                    "INNER JOIN Pet P on WalkService.IdPet = P.IdPet\n" +
                    "INNER JOIN PetOwner PO on P.IdPetOwner = PO.IdPetOwner\n" +
                    "INNER JOIN Province P2 on PO.IdProvince = P2.IdProvince\n" +
                    "INNER JOIN Canton C2 on C.IdCanton = C2.IdCanton\n" +
                    "WHERE DATE(StartTime) BETWEEN :StartDate AND :EndDate \n" +
                    "ORDER BY EntryDate ASC");
            query.addParameter("StartDate", StartDate);
            query.addParameter("EndDate", EndDate);

            int price = connection.createQuery("SELECT Value FROM Configuration WHERE Description = :Description")
                    .addParameter("Description", "Precio")
                    .executeScalar(Integer.class);

            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            List<ReportEntry> reports = query.executeAndFetch(ReportEntry.class);

            for(ReportEntry report : reports){
                report.setUnitPrice(price);
                report.setServiceType("Caminata");
            }

            return reports;
        }
        catch (Exception e){
            throw e;
        }
    }

    public String blockCaregiver(int idCaregiver) throws Exception {
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT * \n" +
                "FROM Caregiver \n" +
                "WHERE IdCaregiver = :IdCaregiver");
        query.addParameter("IdCaregiver", idCaregiver);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<Caregiver> caregiver;
        caregiver = query.executeAndFetch(Caregiver.class);

        if (caregiver.isEmpty()) {
            throw new Exception("El cuidador especificado no existe");
        }

        query = connection.createQuery("UPDATE Caregiver " +
                "SET Status = 2 " +
                "WHERE IdCaregiver = :IdCaregiver");
        query.addParameter("IdCaregiver", idCaregiver);
        query.executeUpdate();


        connection.commit();

        ObjectMapper jsonObject = new ObjectMapper();
        ObjectNode objectNode = jsonObject.createObjectNode();
        objectNode.put("status", "OK");

        return objectNode.toString();
    }

    @Override
    public WalkService getServiceFromId(int IdWalkService) throws Exception {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM WalkService WHERE IdWalkService = :IdWalkService");
        query.addParameter("IdWalkService", IdWalkService);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<WalkService> walkService = query.executeAndFetch(WalkService.class);

        if(!walkService.isEmpty()){
            return walkService.get(0);
        }
        else{
            throw new Exception("Servicio no encontrado.");
        }
    }

    @Override
    public WalkServiceDetailed getServiceDetailed(int IdWalkService) throws Exception {

        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT W.StartTime, W.EndTime, W.Price, W.OwnerComments, W.PickUpLocation,\n" +
                "       W.ReportDescription, W.Status AS WalkServiceStatus, W.Rating, C.IdCaregiver, C.Name AS StudentName, C.LastName AS StudentLastName,\n" +
                "       C.Email1 AS EmailStudent, C.Photo AS StudentPhoto, C.WalksQuantity, C.Phone AS PhoneStudent, C.WalksRating, P.Name AS PetName,\n" +
                "       P.Age, P.IdPet, P.Size, P.PetDescription, PO.Name AS ClientName, PO.LastName AS ClientLastName, PO.Email1 AS EmailClient,\n" +
                "       PO.Phone AS ClientPhone, PO.Photo AS ClientPhoto FROM WalkService W\n" +
                "INNER JOIN Caregiver C on W.IdCaregiver = C.IdCaregiver\n" +
                "INNER JOIN Pet P on W.IdPet = P.IdPet\n" +
                "INNER JOIN PetOwner PO on P.IdPetOwner = PO.IdPetOwner\n" +
                "WHERE IdWalkService = :IdWalkService");

        query.addParameter("IdWalkService", IdWalkService);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());

        List<WalkServiceDetailed> walkService = query.executeAndFetch(WalkServiceDetailed.class);

        if(!walkService.isEmpty()){
            WalkServiceDetailed result = walkService.get(0);
            query = connection.createQuery("SELECT Link FROM PetPhoto WHERE IdPet = :IdPet")
                    .addParameter("IdPet", result.getIdPet());
            result.setPhotoLinks(query.executeScalarList(String.class));
            query = connection.createQuery("SELECT BadgeType FROM Badge WHERE IdCaregiver = :IdCaregiver");
            query.addParameter("IdCaregiver", result.getIdCaregiver());
            result.setBadges(query.executeScalarList(Integer.class));
            return result;
        }
        else{
            throw new Exception("Servicio no encontrado.");
        }
    }

    @Override
    public String getServicePrice(String StartTime, String EndTime) {
        try {
            Connection connection = sql2o.beginTransaction();
            int price = connection.createQuery("SELECT Value FROM Configuration WHERE Description = :Description")
                    .addParameter("Description", "Precio")
                    .executeScalar(Integer.class);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long totalPrice = (formatter.parse(EndTime).getTime() - formatter.parse(StartTime).getTime()) / (1000 * 30 * 60) * price;


            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("price", totalPrice);
            return objectNode.toString();

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String insertService(int IdPet, int IdCaregiver, String StartTime, String EndTime, String OwnerComments, String PickUpLocation) {
        try {
            Connection connection = sql2o.beginTransaction();
            int price = connection.createQuery("SELECT Value FROM Configuration WHERE Description = :Description")
                    .addParameter("Description", "Precio")
                    .executeScalar(Integer.class);

            Query query = connection.createQuery("INSERT INTO WalkService(IdPet, IdCaregiver, StartTime, " +
                    "EndTime, Price, OwnerComments, PickUpLocation) VALUES (:IdPet, :IdCaregiver, :StartTime, :EndTime, " +
                    ":Price, :OwnerComments, :PickUpLocation)");


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long totalPrice = (formatter.parse(EndTime).getTime() - formatter.parse(StartTime).getTime()) / (1000 * 30 * 60) * price;

            query.addParameter("IdPet", IdPet);
            query.addParameter("IdCaregiver", IdCaregiver);
            query.addParameter("StartTime", StartTime);
            query.addParameter("EndTime", EndTime);
            query.addParameter("Price", totalPrice);
            query.addParameter("OwnerComments", OwnerComments);
            query.addParameter("PickUpLocation", PickUpLocation);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.toString());
            timeForNewCaregiver = !timeForNewCaregiver;
            return objectNode.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String insertComplaint(int IdService, String Description) throws Exception {
        try {
            Connection connection = sql2o.beginTransaction();

            Query query = connection.createQuery("INSERT INTO Complaint(IdWalkService, Description) " +
                    "VALUES (:IdWalkService, :Description)");
            query.addParameter("IdWalkService", IdService);
            query.addParameter("Description", Description);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.toString());
            return objectNode.toString();

        } catch (Exception e) {
            throw new Exception("Este cuidador ya ha sido denunciado.");
        }
    }

    @Override
    public String updateReport(int IdService, String ReportDescription) throws Exception{
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT COUNT(IdWalkService) FROM WalkService WHERE IdWalkService = :IdWalkService");
        query.addParameter("IdWalkService", IdService);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        int existsService = query.executeScalar(Integer.class);

        if(existsService == 1) {

            query = connection.createQuery("UPDATE WalkService SET ReportDescription = :ReportDescription " +
                    "WHERE IdWalkService = :IdWalkService");

            query.addParameter("ReportDescription", ReportDescription);
            query.addParameter("IdWalkService", IdService);
            query.executeUpdate();
            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.get("status"));
            return objectNode.toString();
        }
        else{
            throw new Exception("No se ha encontrado el servicio.");

        }
    }

    @Override
    public ObjectNode updateRate(int IdService, int Rate) throws Exception {
        try {

            Connection connection = sql2o.beginTransaction();
            Query query = connection.createQuery("SELECT COUNT(IdWalkService) FROM WalkService WHERE IdWalkService = :IdWalkService");
            query.addParameter("IdWalkService", IdService);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            int existsService = query.executeScalar(Integer.class);

            query = connection.createQuery("UPDATE WalkService SET Rating = :Rating, Status = 1 " +
                    "WHERE IdWalkService = :IdWalkService");
            query.addParameter("Rating", Rate);
            query.addParameter("IdWalkService", IdService);
            query.executeUpdate();

            //get caregiver id
            int idCaregiver = connection.createQuery("SELECT IdCaregiver FROM WalkService WHERE IdWalkService = :IdWalkService")
                    .addParameter("IdWalkService", IdService).executeScalar(Integer.class);

            //get pet id
            int idPet = connection.createQuery("SELECT IdPet FROM WalkService WHERE IdWalkService = :IdWalkService")
                    .addParameter("IdWalkService", IdService).executeScalar(Integer.class);

            //get average rating updated
            float averageRating = connection.createQuery("SELECT AVG(Rating) FROM WalkService WHERE IdCaregiver = :IdCaregiver")
                    .addParameter("IdCaregiver", idCaregiver).executeScalar(Float.class);

            //get quantity of walks for caregiver
            int walksQuantityCaregiver = connection.createQuery("SELECT COUNT(IdWalkService) FROM WalkService WHERE IdCaregiver = :IdCaregiver")
                    .addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);

            //get quantity of walks for pet
            int walksQuantityPet = connection.createQuery("SELECT COUNT(IdWalkService) FROM WalkService WHERE IdPet = :IdPet")
                    .addParameter("IdPet", idPet).executeScalar(Integer.class);

            //get quantity of walks with more than five starts
            int walksQuantityFiveStars = connection.createQuery("SELECT COUNT(IdWalkService) FROM WalkService " +
                    "WHERE IdCaregiver = :IdCaregiver AND Rating = 5")
                    .addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);



            //update caregiver
            connection.createQuery("UPDATE Caregiver SET WalksRating = :WalksRating,  WalksQuantity = :WalksQuantity " +
                    "WHERE IdCaregiver = :IdCaregiver")
                    .addParameter("WalksRating", averageRating)
                    .addParameter("WalksQuantity", walksQuantityCaregiver)
                    .addParameter("IdCaregiver", idCaregiver)
                    .executeUpdate();


            //update pet
            connection.createQuery("UPDATE Pet SET WalksQuantity = :WalksQuantity "+
                    "WHERE IdPet = :IdPet")
                    .addParameter("WalksQuantity", walksQuantityPet)
                    .addParameter("IdPet", idPet)
                    .executeUpdate();


            int hasBadgeType1 = connection.createQuery("SELECT COUNT(BadgeType) FROM Badge WHERE BadgeType = 1 " +
                    "AND IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);
            int hasBadgeType2 = connection.createQuery("SELECT COUNT(BadgeType) FROM Badge WHERE BadgeType = 2 " +
                    "AND IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);
            int hasBadgeType3 = connection.createQuery("SELECT COUNT(BadgeType) FROM Badge WHERE BadgeType = 3 " +
                    "AND IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);
            int hasBadgeType4 = connection.createQuery("SELECT COUNT(BadgeType) FROM Badge WHERE BadgeType = 4 " +
                    "AND IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver).executeScalar(Integer.class);


            //update badges
            if(walksQuantityFiveStars > 100 && hasBadgeType1 == 0){
                connection.createQuery("INSERT INTO Badge(IdCaregiver, BadgeType) " +
                        "VALUES(:IdCaregiver, 1)")
                        .addParameter("IdCaregiver", idCaregiver)
                        .executeUpdate();
            }

            if(walksQuantityCaregiver > 50 && hasBadgeType2 == 0){
                connection.createQuery("INSERT INTO Badge(IdCaregiver, BadgeType) " +
                        "VALUES(:IdCaregiver, 2)")
                        .addParameter("IdCaregiver", idCaregiver)
                        .executeUpdate();
            }

            if(walksQuantityCaregiver > 100 && hasBadgeType3 == 0){
                connection.createQuery("INSERT INTO Badge(IdCaregiver, BadgeType) " +
                        "VALUES(:IdCaregiver, 3)")
                        .addParameter("IdCaregiver", idCaregiver)
                        .executeUpdate();
            }

            if(walksQuantityCaregiver > 500 && hasBadgeType4 == 0){
                connection.createQuery("INSERT INTO Badge(IdCaregiver, BadgeType) " +
                        "VALUES(:IdCaregiver, 4)")
                        .addParameter("IdCaregiver", idCaregiver)
                        .executeUpdate();
            }

            String name = connection.createQuery("SELECT Name FROM Caregiver WHERE " +
                    "IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver)
                    .executeScalar(String.class);

            String lastName = connection.createQuery("SELECT LastName FROM Caregiver WHERE " +
                    "IdCaregiver = :IdCaregiver").addParameter("IdCaregiver", idCaregiver)
                    .executeScalar(String.class);

            connection.commit();

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("name", name + " " + lastName);
            objectNode.put("rating", Rate);

            System.out.println(objectNode.toString());
            return objectNode;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public int assignCaregiver(int idPet, int idPetOwner, String startTime, String endTime, String location){
        Connection connection = sql2o.beginTransaction();

        SimpleDateFormat dateTimeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateDifference;
        try {
            java.util.Date startDate = dateTimeformatter.parse(startTime);
            java.util.Date endDate = dateTimeformatter.parse(endTime);
            dateDifference = endDate.getTime()-startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        long timeSlots = dateDifference / 1000 / 60 / 30;

        Query query;
        query = connection.createQuery("SELECT * FROM PetOwner WHERE IdPetOwner = :IdPetOwner");
        query.addParameter("IdPetOwner", idPetOwner);
        PetOwner petOwner = query.executeAndFetchFirst(PetOwner.class);

        query = connection.createQuery("SELECT WalkService.*\n" +
                "FROM WalkService\n" +
                "INNER JOIN Pet P on WalkService.IdPet = P.IdPet\n" +
                "WHERE DATE(StartTime) = DATE(:StartTime) AND P.IdPetOwner = :IdPetOwner;");
        query.addParameter("StartTime", startTime);
        query.addParameter("IdPetOwner", idPetOwner);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<WalkService> walksSameDay = query.executeAndFetch(WalkService.class);

        if (!walksSameDay.isEmpty()){
            query = connection.createQuery("SELECT WalkService.*\n" +
                    "FROM WalkService\n" +
                    "INNER JOIN Pet P on WalkService.IdPet = P.IdPet\n" +
                    "WHERE DATE(StartTime) = DATE(:StartTime) AND P.IdPetOwner = :IdPetOwner;");
            query.addParameter("StartTime", startTime);
            query.addParameter("IdPetOwner", idPetOwner);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            List<WalkService> walksSameTime = query.executeAndFetch(WalkService.class);
            if (walksSameTime.isEmpty()){
                query = connection.createQuery("SELECT IF(COUNT(IdCaregiver)=:RequiredTimeSlots, IdCaregiver, 0)\n" +
                        "FROM Schedule\n" +
                        "WHERE StartTime >= :StartTime AND EndTime <= :EndTime AND IdCaregiver = :IdCaregiver\n" +
                        "GROUP BY IdCaregiver;");
                query.addParameter("RequiredTimeSlots", timeSlots);
                query.addParameter("StartTime", startTime);
                query.addParameter("EndTime", endTime);
                query.addParameter("IdCaregiver", walksSameDay.get(0).getIdCaregiver());
                int checkedId = query.executeScalar(Integer.class);
                if (checkedId != 0){
                    return checkedId;
                }
            }

            query = connection.createQuery(
                    "SELECT FinalLessAssignedCaregiver.IdCaregiver\n" +
                            "FROM (\n" +
                            "         SELECT IF(LessAssignedCaregiver.WalkAmount < 3, LessAssignedCaregiver.IdCaregiver, 0) AS IdCaregiver\n" +
                            "         FROM (\n" +
                            "                  SELECT count(IdWalkService) AS WalkAmount, C.IdCaregiver\n" +
                            "                  FROM WalkService\n" +
                            "                           INNER JOIN Caregiver C on WalkService.IdCaregiver = C.IdCaregiver\n" +
                            "                           INNER JOIN Pet P on WalkService.IdPet = P.IdPet\n" +
                            "                           INNER JOIN PetOwner PO on P.IdPetOwner = PO.IdPetOwner\n" +
                            "                  WHERE WalkService.StartTime <= :StartTime\n" +
                            "                    AND WalkService.EndTime >= :EndTime\n" +
                            "                    AND P.IdPetOwner = :IdPetOwner\n" +
                            "                    AND C.Status = 1\n" +
                            "                  GROUP BY PO.IdPetOwner, IdCaregiver\n" +
                            "                  ORDER BY WalkAmount ASC\n" +
                            "                  LIMIT 1\n" +
                            "              ) AS LessAssignedCaregiver\n" +
                            "         LIMIT 1\n" +
                            "     ) AS FinalLessAssignedCaregiver;"
            );
            query.addParameter("StartTime", startTime);
            query.addParameter("EndTime", endTime);
            query.addParameter("IdPetOwner", idPetOwner);

            int idCaregiver = query.executeAndFetchFirst(Integer.class);


//      Is there a caregiver assigned to less than 3 of the owner's pets?
            if (idCaregiver != 0){
                return idCaregiver;
            }
        }

        List<Caregiver> caregiversAvailable;

        query = connection.createQuery(
                "SELECT Caregiver.*\n" +
                        "FROM Caregiver\n" +
                        "    INNER JOIN PetOwner PO on Caregiver.IdProvince = PO.IdProvince\n" +
                        "WHERE PO.IdPetOwner = :IdPetOwner;"
        );

        query.addParameter("IdPetOwner", idPetOwner);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        caregiversAvailable = query.executeAndFetch(Caregiver.class);


        if (caregiversAvailable.isEmpty()){ // There aren't caregivers in the same province as the pet owner
            query = connection.createQuery(
                    "SELECT Caregiver.*\n" +
                            "FROM Caregiver\n" +
                            "    INNER JOIN ProvinceXCaregiver PXC on Caregiver.IdCaregiver = PXC.IdCaregiver\n" +
                            "    INNER JOIN PetOwner PO on PO.IdProvince = PXC.IdProvince\n" +
                            "WHERE PO.IdPetOwner = :IdPetOwner;"
            );

            query.addParameter("IdPetOwner", idPetOwner);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            caregiversAvailable = query.executeAndFetch(Caregiver.class);
        }

        if (caregiversAvailable.isEmpty()){ // There aren't caregivers neither in the owners province, nor available to care in it
            return 0;
        }


        query = connection.createQuery(
                "SELECT Caregiver.*\n" +
                        "FROM Caregiver\n" +
                        "    INNER JOIN (SELECT IdCaregiver, COUNT(IdCaregiver) AS TimeSlots\n" +
                        "                FROM Schedule\n" +
                        "                WHERE StartTime >= :StartTime AND EndTime <= :EndTime\n" +
                        "                GROUP BY IdCaregiver) AS AvailableCaregivers ON Caregiver.IdCaregiver = AvailableCaregivers.IdCaregiver\n" +
                        "WHERE AvailableCaregivers.TimeSlots = :RequiredTimeSlots;"
        );

        query.addParameter("StartTime", startTime);
        query.addParameter("EndTime", endTime);
        query.addParameter("RequiredTimeSlots", timeSlots);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<Caregiver> caregiversAvailableInTimeSlotAccordingToSchedule;
        caregiversAvailableInTimeSlotAccordingToSchedule = query.executeAndFetch(Caregiver.class);

        query = connection.createQuery("SELECT Caregiver.*\n" +
                "FROM Caregiver\n" +
                "INNER JOIN WalkService WS on Caregiver.IdCaregiver = WS.IdCaregiver\n" +
                "WHERE (StartTime <= :StartTime AND EndTime >= :StartTime) OR (StartTime <= :EndTime AND EndTime >= :EndTime) ;");
        query.addParameter("StartTime", startTime);
        query.addParameter("EndTime", endTime);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<Caregiver> caregiversAlreadyAssignedInTimeSlot;
        caregiversAlreadyAssignedInTimeSlot = query.executeAndFetch(Caregiver.class);

        ArrayList<Caregiver> caregiversAvailableInTimeSlot = new ArrayList<>();

        for (Caregiver caregiver :
                caregiversAvailableInTimeSlotAccordingToSchedule) {
            boolean busy = false;
            for (Caregiver caregiverAlreadyAssigned : caregiversAlreadyAssignedInTimeSlot){
                if (caregiver.getIdCaregiver() == caregiverAlreadyAssigned.getIdCaregiver()){
                    busy = true;
                    break;
                }
            }
            if (!busy){
                caregiversAvailableInTimeSlot.add(caregiver);
            }
        }

        if (timeForNewCaregiver){
            boolean newCaregiversAvailable = false;
            for (Caregiver caregiver : caregiversAvailable) {
                if (caregiver.getWalksQuantity() == 0){
                    newCaregiversAvailable = true;
                    break;
                }
            }
            if (newCaregiversAvailable){
                List<Caregiver> newCaregivers = new ArrayList<>();
                for (Caregiver caregiver : caregiversAvailable){
                    if (caregiver.getWalksQuantity() == 0){
                        newCaregivers.add(caregiver);
                    }
                }
                List<Caregiver> newCaregiversInTimeSlot = new ArrayList<>();
                for (Caregiver caregiver : caregiversAvailableInTimeSlot){
                    for (Caregiver newCaregiver : newCaregivers){
                        if (newCaregiver.getIdCaregiver() == caregiver.getIdCaregiver()){
                            newCaregiversInTimeSlot.add(newCaregiver);
                        }
                    }
                }
                if (!newCaregiversInTimeSlot.isEmpty()){
                    List<Caregiver> newCaregiversInTimeSlotInLocation = new ArrayList<>();
                    for (Caregiver caregiver : newCaregiversInTimeSlot){
                        if (caregiver.getIdCanton() == petOwner.getIdCanton()){
                            newCaregiversInTimeSlotInLocation.add(caregiver);
                        }

                    }
                    if (!newCaregiversInTimeSlotInLocation.isEmpty()){
                        newCaregiversInTimeSlot = newCaregiversInTimeSlotInLocation;
                    }
                    Caregiver leastRecentCaregiver = newCaregiversInTimeSlot.get(0);
                    for (Caregiver caregiver : newCaregiversInTimeSlot){
                        if (caregiver.getInscriptionDate().getTime() < leastRecentCaregiver.getInscriptionDate().getTime()){
                            leastRecentCaregiver = caregiver;
                        }
                    }
                    return leastRecentCaregiver.getIdCaregiver();
                }
            }
        }

        query = connection.createQuery("SELECT Badge.*\n" +
                "FROM Badge\n" +
                "         INNER JOIN (SELECT IdCaregiver, COUNT(IdCaregiver) AS TimeSlots\n" +
                "                     FROM Schedule\n" +
                "                     WHERE StartTime >= :StartTime AND EndTime <= :EndTime\n" +
                "                     GROUP BY IdCaregiver) AS AvailableCaregivers on Badge.IdCaregiver = AvailableCaregivers.IdCaregiver\n" +
                "WHERE AvailableCaregivers.TimeSlots = :RequiredTimeSlots" );
        query.addParameter("StartTime", startTime);
        query.addParameter("EndTime", endTime);
        query.addParameter("RequiredTimeSlots", timeSlots);

        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<Badge> badgesForAvailableCaregivers;
        badgesForAvailableCaregivers = query.executeAndFetch(Badge.class);

        if (!caregiversAvailableInTimeSlot.isEmpty()) {
            Caregiver selectedCaregiver = caregiversAvailableInTimeSlot.get(0);
            float selectedCaregiverScore = 0;
            for (Caregiver caregiver : caregiversAvailableInTimeSlot) {
                float score = caregiver.getWalksRating() * 0.5f;
                if (caregiver.getIdCanton() == petOwner.getIdCanton()) {
                    score += 0.1;
                }
                for (Badge badge : badgesForAvailableCaregivers) {
                    if (badge.getIdCaregiver() == caregiver.getIdCaregiver()) {
                        if (badge.getBadgeType() == 1 || badge.getBadgeType() == 2 ||
                                badge.getBadgeType() == 3 || badge.getBadgeType() == 4) {
                            score += 0.1;
                        }
                    }
                }
                if (score > selectedCaregiverScore){
                    selectedCaregiver = caregiver;
                    selectedCaregiverScore = score;
                } else if (score == selectedCaregiverScore){
                    if (caregiver.getLastName().compareToIgnoreCase(selectedCaregiver.getLastName()) < 0){
                        selectedCaregiver = caregiver;
                        selectedCaregiverScore = score;

                    }
                }
            }
            return selectedCaregiver.getIdCaregiver();
        }
        return 0;
    }


    @Override
    public String getClientIdType(String LoginData, String Password) throws Exception{
        Connection connection = sql2o.open();
        int IsPetOwner = 0;
        int IsCaregiver = 0;
        int IsAdministrator = 0;
        int IdClient = -1;
        String ClientType = "";
        String PasswordHashed;

        Query query = connection.createQuery("SELECT COUNT(IdPetOwner) FROM PetOwner WHERE Email1 = :Email1");
        query.addParameter("Email1", LoginData);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        IsPetOwner = query.executeScalar(Integer.class);

        query = connection.createQuery("SELECT COUNT(IdCaregiver) FROM Caregiver WHERE IdStudent = :IdStudent");
        query.addParameter("IdStudent", LoginData);
        IsCaregiver = query.executeScalar(Integer.class);

        query = connection.createQuery("SELECT COUNT(IdAdministrator) FROM Administrator WHERE Username = :Username");
        query.addParameter("Username", LoginData);
        IsAdministrator = query.executeScalar(Integer.class);

        if(IsPetOwner == 1){
            query = connection.createQuery("SELECT IdPetOwner FROM PetOwner WHERE Email1 = :Email1");
            query.addParameter("Email1", LoginData);
            IdClient = query.executeScalar(Integer.class);
            query = connection.createQuery("SELECT Password FROM PetOwner WHERE Email1 = :Email1");
            query.addParameter("Email1", LoginData);
            PasswordHashed = query.executeScalar(String.class);

            if(BCrypt.checkpw(Password, PasswordHashed)){
                System.out.println("Password matched for pet owner");
                ClientType = "Client";

            }
            else{
                throw new Exception("La contraseña no es válida.");
            }
        }

        else if(IsCaregiver == 1){
            query = connection.createQuery("SELECT IdCaregiver FROM Caregiver WHERE IdStudent = :IdStudent");
            query.addParameter("IdStudent", LoginData);
            IdClient = query.executeScalar(Integer.class);
            query = connection.createQuery("SELECT Password FROM Caregiver WHERE IdStudent = :IdStudent");
            query.addParameter("IdStudent", LoginData);
            PasswordHashed = query.executeScalar(String.class);

            if(BCrypt.checkpw(Password, PasswordHashed)){
                System.out.println("Password matched for caregiver");
                ClientType = "Student";

            }
            else{
                throw new Exception("La contraseña no es válida.");
            }
        }

        else if(IsAdministrator == 1){
            query = connection.createQuery("SELECT IdAdministrator FROM Administrator WHERE Username = :Username");
            query.addParameter("Username", LoginData);
            IdClient = query.executeScalar(Integer.class);
            query = connection.createQuery("SELECT Password FROM Administrator WHERE Username = :Username");
            query.addParameter("Username", LoginData);
            PasswordHashed = query.executeScalar(String.class);

            if(BCrypt.checkpw(Password, PasswordHashed)){
                System.out.println("Password matched for administrator");
                ClientType = "Admin";
            }
            else{
                throw new Exception("La contraseña no es válida.");
            }
        }

        else{

            throw new Exception("Usuario no encontrado.");
        }

        ObjectMapper jsonObject = new ObjectMapper();
        ObjectNode objectNode = jsonObject.createObjectNode();
        objectNode.put("id", IdClient);
        objectNode.put("type", ClientType);
        objectNode.put("token", Token.generateToken(IdClient, ClientType));

        return objectNode.toString();

    }
    public String insertHours(int idCaregiver, String startTime, String endTime) throws Exception {
        SimpleDateFormat dateTimeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateDifference;
        Date startDate = dateTimeformatter.parse(startTime);
        Date endDate = dateTimeformatter.parse(endTime);
        dateDifference = endDate.getTime()-startDate.getTime();
        if (dateDifference < 0){
            throw new Exception("La hora de finalización se encuentra antes de la de inicio.");
        }
        long timeSlots = dateDifference / 1000 / 60 / 30;

        Connection connection = sql2o.beginTransaction();
        Query query;

        for (int i = 0; i < timeSlots; i++) {
            query = connection.createQuery("INSERT INTO Schedule(IdCaregiver, StartTime, EndTime)\n" +
                    "VALUE (:IdCaregiver, :StartTime, :EndTime)");
            query.addParameter("IdCaregiver", idCaregiver);
            query.addParameter("StartTime",dateTimeformatter.format(startDate));
            startDate.setTime(startDate.getTime()+30*60*1000);
            query.addParameter("EndTime", dateTimeformatter.format(startDate));
            try {
                query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
                query.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ObjectMapper jsonObject = new ObjectMapper();
        ObjectNode objectNode = jsonObject.createObjectNode();
        objectNode.put("status", "OK");

        return objectNode.toString();
    }

    public List getAllScheduleEntries(int idCaregiver, String datetime) {
        Connection connection = sql2o.open();
        Query query = connection.createQuery("SELECT * FROM Schedule WHERE IdCaregiver = :IdCaregiver " +
                "AND DATE(StartTime) = DATE(:StartTime)"
        );
        query.addParameter("IdCaregiver", idCaregiver);
        query.addParameter("StartTime", datetime);
        query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
        List<Schedule> schedules = query.executeAndFetch(Schedule.class);

        return schedules;
    }


}
