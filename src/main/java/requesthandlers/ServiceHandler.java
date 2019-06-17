package requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dataobjects.Model;
import dataobjects.PetOwner;
import dataobjects.WalkService;
import routing.CustomResponse;
import routing.ResponseCreator;

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


    public static ResponseCreator updateReport(Model model, int idService, String description) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WalkService walkService = mapper.readValue(description, WalkService.class);
            String jsonString = model.updateReport(idService, walkService.getReportDescription());
            return CustomResponse.ok(jsonString);
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getCaregiverAvailability(Model model, int idPet, int idPetOwner, String startTime, String endTime, String location){
        int caregiverId = model.assignCaregiver(idPet, idPetOwner, startTime, endTime, location);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        if (caregiverId!=0){
            objectNode.put("caregiver", caregiverId);
            System.out.println(objectNode.toString());
            return CustomResponse.ok(objectNode.toString());
        }
        return CustomResponse.error(402, "No existen cuidadores disponibles en la franja horaria especificada");


    }
}
