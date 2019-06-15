package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dataobjects.Administrator;
import dataobjects.Model;
import dataobjects.PetOwner;
import routing.CustomResponse;
import routing.ResponseCreator;

import java.io.IOException;

public class SignUpHandler {

    public static ResponseCreator insertPetOwner(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        PetOwner creation = null;
        try {
            creation = mapper.readValue(requestBody, PetOwner.class);
            String jsonString = model.insertPetOwner(creation.getIdProvince(), creation.getIdCanton(), creation.getName(), creation.getLastName(),
                                creation.getEmail1(), creation.getEmail2(), creation.getPhone(), creation.getPhoto(),
                                creation.getInscriptionDate(), creation.getPersonalDescription(), creation.getPassword());
            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }

}
