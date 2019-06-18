import dataobjects.Model;
import org.mindrot.jbcrypt.BCrypt;
import org.sql2o.Sql2o;
import routing.Application;
import routing.ResponseCreator;
import spark.servlet.SparkApplication;
import sql2omodel.Sql2oModel;
import com.fasterxml.jackson.databind.*;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.sql.DataSource;

import java.util.Properties;

import static spark.Spark.*;

public class Main implements SparkApplication {


    public static void main(String[] args) {
        //get("/hello", (req, res) -> "Madriz Guapo");

//        String pass = "password";
//        String encrypted_pass = BCrypt.hashpw(pass, BCrypt.gensalt());
//        System.out.println(encrypted_pass);
//        System.out.println(BCrypt.checkpw(pass, "$2a$10$sIv5Hc0/j97NMkLomQnZruBjLp4r4Eg1QStmGzAuvnBl5kLbtYOcq"));






    new Main().init();

    }

    @Override
    public void init() {
        port(8080);

        String url = System.getProperty("cloudsql");

//        HikariConfig config = new HikariConfig();
////
//        config.setJdbcUrl(String.format("jdbc:mysql://google/%s", "DigiPet"));
//        config.setUsername("root"); // e.g. "root", "postgres"
//        config.setPassword("digipet12345"); // e.g. "my-password"
////
//        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
//        config.addDataSourceProperty("cloudSqlInstance", "/cloudsql/digipet:us-east1:digipet-mysql");
//        config.addDataSourceProperty("useSSL", "false");
//        DataSource pool = new HikariDataSource(config);

        Sql2o sql2o = new Sql2o("jdbc:mysql://35.237.93.122:3306/DigiPet", "root", "digipet12345");
//        Sql2o sql2o = new Sql2o(pool);

//        Sql2o sql2o = new Sql2o(url, "root", "digipet12345");
        Model model = new Sql2oModel(sql2o);


        Application app = new Application(model);
        app.init();

        //get all pets from client (using HTTP get method)
        get("/clients/:id/pets", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            int IdPetOwner = Integer.parseInt(request.params(":id"));
            response.status(200);
            response.type("application/json");
            String jsonString = mapper.writeValueAsString(model.getAllPetsFromOwner(IdPetOwner));

            System.out.println(jsonString);
            return jsonString;
        });


    }

    // Use Servlet annotation to define the Spark filter without web.xml:
    @WebFilter(
            filterName = "SparkInitFilter", urlPatterns = {"/*"},
            initParams = {
                    @WebInitParam(name = "applicationClass", value = "Main")
            })
    public static class SparkInitFilter extends spark.servlet.SparkFilter {
    }

}

