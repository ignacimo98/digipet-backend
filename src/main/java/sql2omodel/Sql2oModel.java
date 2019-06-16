package sql2omodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import dataobjects.Administrator;
import dataobjects.Pet;
import dataobjects.Model;
import dataobjects.Province;
import org.mindrot.jbcrypt.BCrypt;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.Date;
import java.sql.SQLException;
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
            connection.createQuery("INSERT INTO Administrator(Username, Email, Password, Status) VALUES (:Username, :Email, :Password, :Status)")
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
    public List getPetFromId(int IdPet) {
        try (Connection connection = sql2o.open()){
            Query query = connection.createQuery("SELECT * FROM Pet WHERE IdPet = :IdPet");
            query.addParameter("IdPet", IdPet);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            return query.executeAndFetch(Pet.class);
        }
        catch (Exception e){
            System.out.println("Error");
            throw e;
        }
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

        ObjectMapper jsonObject = new ObjectMapper();
        ObjectNode objectNode = jsonObject.createObjectNode();
        objectNode.put("id",IdClient);
        objectNode.put("type",ClientType);

        System.out.println(objectNode.toString());
        return objectNode.toString();

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

}
