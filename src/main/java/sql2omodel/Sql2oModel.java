package sql2omodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import dataobjects.*;
import org.mindrot.jbcrypt.BCrypt;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import javax.swing.text.html.parser.Entity;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sql2oModel implements Model {
    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o){
        this.sql2o = sql2o;
    }


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
    public List getAllPetsFromOwner(int IdPetOwner) {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT * FROM Pet WHERE IdPetOwner = :IdPetOwner");
            query.addParameter("IdPetOwner", IdPetOwner);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            return query.executeAndFetch(Pet.class);
        }
        catch (Exception e){
            System.out.println("Error");
            throw e;
        }
    }

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
            return result;
        }
        else{
            throw new Exception("Estudiante no encontrado.");
        }
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
    public List getClientIdType(String LoginData, String Password) throws Exception{
        Connection connection = sql2o.open();
        int IsPetOwner = 0;
        int IsCaregiver = 0;
        int IsAdministrator = 0;
        int IdClient = -1;
        String ClientType = "";
        String PasswordHashed;
        String JsonString;



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

        List<String> result = new ArrayList<>();
        result.add(Integer.toString(IdClient));
        result.add(ClientType);

        System.out.println(result);
        return result;

    }

    @Override
    public String insertCaregiver(int IdStudent, int IdUniversity, int IdProvince, int IdCanton, String Name,
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

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.toString());
            return objectNode.toString();
        }
        else{
            throw new Exception("Ya existe un usuario con este email. Por favor intente con otro.");

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

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("status", "OK");
            System.out.println(objectNode.toString());
            return objectNode.toString();
        }
        else{
            throw new Exception("Ya existe un usuario con este email. Por favor intente con otro.");
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
            long totalPrice = (formatter.parse(EndTime).getTime() - formatter.parse(StartTime) .getTime())/(1000*60*60) * 2 * price;

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
            return objectNode.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int assignCaregiver(int IdPet, int IdPetOwner, Date StartTime, Date EndTime, String Location){
        Connection connection = sql2o.beginTransaction();
        Query query = connection.createQuery("SELECT IdCaregiver FROM Caregiver WHERE Email1 = :Email1");



        return 0;

    }

}
