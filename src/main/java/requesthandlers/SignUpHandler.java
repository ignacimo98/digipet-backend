package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dataobjects.Administrator;
import dataobjects.Caregiver;
import dataobjects.Model;
import dataobjects.PetOwner;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import routing.CustomResponse;
import routing.ResponseCreator;

import java.io.IOException;
import java.security.Key;

public class SignUpHandler {

    public static ResponseCreator insertPetOwner(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        PetOwner creation = null;
        try {
            creation = mapper.readValue(requestBody, PetOwner.class);
            String jsonString = model.insertPetOwner(creation.getIdProvince(), creation.getIdCanton(), creation.getName(), creation.getLastName(),
                                creation.getEmail1(), creation.getEmail2(), creation.getPhone(), creation.getPhoto(),
                                creation.getPersonalDescription(), creation.getPassword());

            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }
    public static ResponseCreator insertCaregiver(Model model, String requestBody){
        ObjectMapper mapper = new ObjectMapper();

        Caregiver creation = null;
        try {
            creation = mapper.readValue(requestBody, Caregiver.class);
            String jsonString = model.insertCaregiver(creation.getIdStudent(), creation.getIdUniversity(), creation.getIdProvince(),
                    creation.getIdCanton(), creation.getName(), creation.getLastName(), creation.getEmail1(), creation.getEmail2(),
                    creation.getPhoto(), creation.getPersonalDescription(), creation.getPhone(),
                    creation.getWorksInOtherProvince(), creation.getPassword(), creation.getOtherProvincesId());
            return CustomResponse.ok(jsonString);

        } catch (Exception e) {
            return CustomResponse.error(402, e.getMessage());
        }
    }


}
