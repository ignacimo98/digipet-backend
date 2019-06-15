package routing;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CustomResponse {

    public static ResponseCreator ok(String body) {
        return (req, res) -> {
            res.status(200);
            res.type("application/json");

            return body;
        };
    }

    public static ResponseCreator ok(int body) {
        return (req, res) -> {
            res.status(200);
            res.type("application/json");

            return body;
        };
    }

    public static ResponseCreator error(int errorCode, String errorDescription){
        return (req, res) -> {
            ObjectMapper jsonObject = new ObjectMapper();
            ObjectNode objectNode = jsonObject.createObjectNode();
            objectNode.put("error",errorDescription);
            res.status(errorCode);
            res.type("application/json");

            return objectNode.toString();
        };
    }

    public static ResponseCreator badRequest(String body) {
        return (req, res) -> {
            res.status(400);
            res.type("application/json");

            return body;
        };
    }
}
