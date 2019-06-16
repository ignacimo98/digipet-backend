package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Administrator;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;

import java.io.IOException;

public class AdministratorHandler {

    public static ResponseCreator getAdmin(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getAdminFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getAllAdmins(Model model) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(model.getAllAdmins());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonString = "{}";
        }

        System.out.println(jsonString);
        return  CustomResponse.ok(jsonString);
    }

    public static ResponseCreator insertAdmin(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        Administrator creation = null;
        try {
            creation = mapper.readValue(requestBody, Administrator.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int id = model.createAdmin(creation.getUsername(), creation.getEmail(), creation.getPassword(), creation.getStatus());
        return CustomResponse.ok(id);
    }

}
