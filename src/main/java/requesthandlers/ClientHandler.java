package requesthandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;

public class ClientHandler {

    public static ResponseCreator getPetOwner(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getPetOwnerFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getServices(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getServicesForPetOwner(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getPets(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getAllPetsFromOwner(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator changePetOwnerStatus(Model model, int id, String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            boolean status = mapper.readTree(requestBody).get("status").asBoolean();
            return CustomResponse.ok(model.changePetOwnerStatus(id, status));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }
}
