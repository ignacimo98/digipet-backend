package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;

public class LoginHandler {

    public static ResponseCreator getClientIdType(Model model, String Email, String Password) {

        String jsonString;
        try {
            jsonString = model.getClientIdType(Email, Password);
            return CustomResponse.ok(jsonString);
        } catch (Exception e) {
            return CustomResponse.error(401, e.getMessage());
        }
    }
}
