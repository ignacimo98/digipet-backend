package requesthandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dataobjects.Model;
import dataobjects.PetOwner;
import dataobjects.WalkService;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import routing.CustomResponse;
import routing.ResponseCreator;
import twitter4j.TwitterHandler;
import twitter4j.Twitterer;

import java.io.IOException;

public class ServiceHandler {

    public static ResponseCreator getService(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getServiceFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getServicePrice(Model model, String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String startTime = mapper.readTree(requestBody).get("startTime").asText();
            String endTime = mapper.readTree(requestBody).get("endTime").asText();

            return CustomResponse.ok(model.getServicePrice(startTime, endTime));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }


    public static ResponseCreator insertComplaint(Model model, int idService, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        try {
            String description = mapper.readTree(requestBody).get("Description").asText();
            String jsonString = model.insertComplaint(idService, description);

            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }

    public static ResponseCreator insertService(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        WalkService creation = null;
        try {
            creation = mapper.readValue(requestBody, WalkService.class);
            String jsonString = model.insertService(creation.getIdPet(), creation.getIdCaregiver(), creation.getStartTime(),
                    creation.getEndTime(), creation.getOwnerComments(), creation.getPickUpLocation());

            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }


    public static ResponseCreator updateReport(Model model, int idService, String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String description = mapper.readTree(requestBody).get("ReportDescription").asText();
            String jsonString = model.updateReport(idService, description);
            return CustomResponse.ok(jsonString);
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator updateRate(Model model, int idService, String requestBody) {
        ObjectNode jsonObject;
        ObjectMapper mapper = new ObjectMapper();

        try {
            int rate = mapper.readTree(requestBody).get("Rating").asInt();
            jsonObject = model.updateRate(idService, rate);
            TwitterHandler.postTweet(jsonObject.get("name").asText(), rate);

            return CustomResponse.ok(jsonObject.toString());
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getCaregiverAvailability(Model model, String body){
        ObjectMapper objectMapper = new ObjectMapper();
        int caregiverId = 0;
        try {
            JsonNode node = objectMapper.readTree(body);
            caregiverId = model.assignCaregiver(node.get("IdPet").asInt(), node.get("IdPetOwner").asInt(), node.get("StartTime").asText(), node.get("EndTime").asText(), node.get("location").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObjectMapper objectMapper1 = new ObjectMapper();
        ObjectNode objectNode = objectMapper1.createObjectNode();

        if (caregiverId!=0){
            objectNode.put("caregiver", caregiverId);
            System.out.println(objectNode.toString());
            return CustomResponse.ok(objectNode.toString());
        }
        return CustomResponse.error(402, "No existen cuidadores disponibles en la franja horaria especificada");


    }
}
