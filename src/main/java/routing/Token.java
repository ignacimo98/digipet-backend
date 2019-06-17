package routing;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import java.security.Key;

public class Token {

    public static String generateToken(int id, String type){
        try {
            JwtClaims claims = new JwtClaims();
            claims.setExpirationTimeMinutesInTheFuture(240);
            claims.setSubject("authentication");
            claims.setClaim("id", id);
            claims.setClaim("type", type);

            String secret = "secret";
            Key key = new HmacKey(secret.getBytes("UTF-8"));

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setKey(key);
            jws.setDoKeyValidation(false); // relaxes the key length requirement

            return jws.getCompactSerialization();

        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
