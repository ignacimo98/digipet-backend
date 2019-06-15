package sql2omodel;

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

import java.sql.SQLException;
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

        JsonString = "{'id': " + IdClient + ", 'type': " + ClientType + "}";
        System.out.println(JsonString);
        return JsonString;

    }

}
