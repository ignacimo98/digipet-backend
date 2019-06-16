package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import dataobjects.Pet;
import dataobjects.PetOwner;
import routing.CustomResponse;
import routing.ResponseCreator;

public class PetHandler {

    public static ResponseCreator getPet(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getPetFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator insertPet(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        Pet creation = null;
        try {
            creation = mapper.readValue(requestBody, Pet.class);
            String jsonString = model.insertPet(creation.getIdPetOwner(), creation.getName(), creation.getAge(),
                    creation.getSize(), creation.getPetDescription(), creation.getPhotoLinks());
            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }

    public static ResponseCreator getServices(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getServicesForPet(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }
}
