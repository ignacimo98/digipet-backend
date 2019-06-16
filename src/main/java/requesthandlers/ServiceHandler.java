package requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataobjects.Model;
import dataobjects.PetOwner;
import dataobjects.WalkService;
import routing.CustomResponse;
import routing.ResponseCreator;

public class ServiceHandler {
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
}
