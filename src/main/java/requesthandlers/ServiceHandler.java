package requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import dataobjects.PetOwner;
import dataobjects.WalkService;
import routing.CustomResponse;
import routing.ResponseCreator;

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

    public static ResponseCreator updateReport(Model model, int idService, String description){
        ObjectMapper mapper = new ObjectMapper();
        try {
            WalkService walkService = mapper.readValue(description, WalkService.class);
            String jsonString = model.updateReport(idService, walkService.getReportDescription());
            return CustomResponse.ok(jsonString);
        }
        catch (Exception e){
            return CustomResponse.error(404, e.getMessage());
        }
    }
}
