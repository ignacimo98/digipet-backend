package routing;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.security.Key;
import java.util.HashMap;

import static spark.Spark.halt;

public final class TokenFilter {



    public final static void apply() {
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                System.out.println(request.pathInfo());
                if (!(request.pathInfo().equals("/login") ||
                        request.pathInfo().equals("/signup/clients") ||
                        request.pathInfo().equals("/signup/students")||
                        request.requestMethod().equals("OPTIONS"))){

                    String secret = "secret";
                    String jwt = request.headers("Authorization");

                    Key key = new HmacKey(secret.getBytes("UTF-8"));
                    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                            .setRequireExpirationTime()
                            .setAllowedClockSkewInSeconds(30)
                            .setRequireSubject()
                            .setVerificationKey(key)
                            .setRelaxVerificationKeyValidation() // relaxes key length requirement
                            .build();

                    try {
                        JwtClaims processedClaims = jwtConsumer.processToClaims(jwt);
                        request.attribute("id", processedClaims.getClaimValue("id"));
                        request.attribute("type", processedClaims.getClaimValue("type"));

                    }
                    catch (InvalidJwtException e) {
                        halt(403, "{\"error\": \"No autorizado\", \"logout\": true }");
                    }
                }

            }
        };
        Spark.before(filter);
    }
}
