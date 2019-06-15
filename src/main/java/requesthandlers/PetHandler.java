package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;

public class PetHandler {
    public static ResponseCreator getPet(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getPetFromId(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CustomResponse.badRequest(e.toString());
        }
    }
}
