package requesthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dataobjects.Model;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import routing.CustomResponse;
import routing.ResponseCreator;

import java.security.Key;
import java.util.List;

public class LoginHandler {

    public static ResponseCreator getClientIdType(Model model, String Email, String Password) {

        try {
            List<String> clientIdType = model.getClientIdType(Email, Password);

            JwtClaims claims = new JwtClaims();
            claims.setExpirationTimeMinutesInTheFuture(240);
            claims.setSubject("authentication");
            claims.setClaim("id",clientIdType.get(0));
            claims.setClaim("type", clientIdType.get(1));

            String secret = "secret";
            Key key = new HmacKey(secret.getBytes("UTF-8"));

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setKey(key);
            jws.setDoKeyValidation(false); // relaxes the key length requirement

            String jwt = jws.getCompactSerialization();
            System.out.println(jwt);

            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("id", Integer.parseInt(clientIdType.get(0)));
            objectNode.put("type", clientIdType.get(1));
            objectNode.put("token", jwt);
            System.out.println(objectNode.toString());

            return CustomResponse.ok(objectNode.toString());
        } catch (Exception e) {
            return CustomResponse.error(401, e.getMessage());
        }
    }
}
