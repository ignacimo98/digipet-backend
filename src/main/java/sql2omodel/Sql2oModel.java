package sql2omodel;

import dataobjects.Administrator;
import dataobjects.Pet;
import dataobjects.Model;
import org.sql2o.Connection;
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
    public int createAdmin(String username, String email, String password, int status) {
        try (Connection connection = sql2o.beginTransaction()){
            connection.createQuery("INSERT INTO Administrator(Username, Email, Password, Status) VALUES (:Username, :Email, :Password, :Status)")
                    .addParameter("Username", username)
                    .addParameter("Email", email)
                    .addParameter("Password", password)
                    .addParameter("Status", status)
                    .executeUpdate();
            connection.commit();
        }

        return 0;
    }

    @Override
    public List<Administrator> getAllAdmins() {
        try (Connection connection = sql2o.open()){
            System.out.println("HERE");
            //works
            connection.createQuery("create table test_table(id int, test_col int)").executeUpdate();
            System.out.println("HERE");
            //method that fails
            List<Administrator> results = connection.createQuery("SELECT * FROM Administrator")
                    .executeAndFetch(Administrator.class);

            System.out.println("HERE");
            for(Administrator result : results){
                System.out.println(result.getUsername());
            }
            return results;
        }
        catch (Exception e){
            System.out.println("Error");
            throw e;
        }
    }
}
