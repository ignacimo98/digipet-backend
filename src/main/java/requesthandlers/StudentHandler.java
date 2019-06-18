package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;


import java.io.IOException;

public class StudentHandler {
    public static ResponseCreator getCaregiver(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getCaregiverFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getServices(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getServicesForCaregiver(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator insertSchedule(Model model, int idStudent, String body){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(body);
            return CustomResponse.ok(mapper.writeValueAsString(model.insertHours(idStudent,node.get("startTime").asText(), node.get("endTime").asText())));
        } catch (IOException e) {
            return CustomResponse.error(300, "Lo sentimos, su solicitud no puede ser procesada.");
        } catch (Exception e){
            return CustomResponse.error(300, e.getMessage());
        }
    }

    public static ResponseCreator changeCaregiverStatus(Model model, int id, String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            int status = mapper.readTree(requestBody).get("status").asInt();
            return CustomResponse.ok(model.changeCaregiverStatus(id, status));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getSchedulesDateAndIdCaregiver(Model model, int idCaregiver, String date){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(model.getAllScheduleEntries(idCaregiver, date));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonString = "{}";
        }

        System.out.println(jsonString);
        return  CustomResponse.ok(jsonString);
    }


}
