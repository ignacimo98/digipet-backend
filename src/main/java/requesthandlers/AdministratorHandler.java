package requesthandlers;

import XLS.XLSWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.org.apache.xpath.internal.operations.Mod;
import dataobjects.Administrator;
import dataobjects.Model;
import dataobjects.ReportEntry;
import routing.CustomResponse;
import routing.ResponseCreator;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.List;

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

    public static ResponseCreator getAllCaregivers(Model model) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(model.getAllCaregivers());
        } catch (Exception e) {
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

    public static ResponseCreator blockCaregiver(Model model, int idCaregiver){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message;
        try {
            message = model.blockCaregiver(idCaregiver);
            return CustomResponse.ok(message);
        } catch (Exception e){
            return CustomResponse.error(300, e.getMessage());
        }
    }

    public static ResponseCreator getComplaints(Model model) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getComplaints()));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getReport(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String startDate = mapper.readTree(requestBody).get("startDate").asText();
            String endDate = mapper.readTree(requestBody).get("endDate").asText();
            List<ReportEntry> report = model.getReport(startDate, endDate);
            XLSWriter.generateExcel(report, startDate, endDate);

            return CustomResponse.ok(mapper.writeValueAsString(report));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator setPrice(Model model, String requestBody){
        ObjectMapper objectMapper = new ObjectMapper();
        String message;
        try {
            int price = objectMapper.readTree(requestBody).get("price").asInt();
            message = model.setPrice(price);
            return CustomResponse.ok(message);
        } catch (Exception e){
            return CustomResponse.error(300, e.getMessage());
        }
    }

}
