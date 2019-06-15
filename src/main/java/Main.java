import dataobjects.Administrator;
import dataobjects.Model;
import org.sql2o.Sql2o;
import routing.Application;
import routing.ResponseCreator;
import sql2omodel.Sql2oModel;
import com.fasterxml.jackson.databind.*;
import spark.Route;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        //get("/hello", (req, res) -> "Madriz Guapo");

        Sql2o sql2o = new Sql2o("jdbc:mysql://35.222.98.163:3306/DigiPet", "root", "digipet12345");
        Model model = new Sql2oModel(sql2o);

        Application app = new Application(model);
        app.init();


        //inserts and admin
        post("/administrators", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            Administrator creation = mapper.readValue(request.body(), Administrator.class);

            //int id = model.createAdmin("user", "djaidjias", "sdada", 0);
            int id = model.createAdmin(creation.getUsername(), creation.getEmail(), creation.getPassword(), creation.getStatus());
            response.status(200);
            response.type("application/json");
            return id;
        });

        //get all admins (using HTTP get method)
        get("/administrators", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            response.status(200);
            response.type("application/json");
            String jsonString = mapper.writeValueAsString(model.getAllAdmins());

            System.out.println(jsonString);
            return jsonString;
        });

    }

}

