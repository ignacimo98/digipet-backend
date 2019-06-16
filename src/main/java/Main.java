import dataobjects.Administrator;
import dataobjects.Model;
import org.mindrot.jbcrypt.BCrypt;
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

//        String pass = "password";
//        String encrypted_pass = BCrypt.hashpw(pass, BCrypt.gensalt());
//        System.out.println(encrypted_pass);
//        System.out.println(BCrypt.checkpw(pass, "$2a$10$sIv5Hc0/j97NMkLomQnZruBjLp4r4Eg1QStmGzAuvnBl5kLbtYOcq"));

        Sql2o sql2o = new Sql2o("jdbc:mysql://35.237.93.122:3306/DigiPet", "root", "digipet12345");
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

}

