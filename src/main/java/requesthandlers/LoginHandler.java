package requesthandlers;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;

public class LoginHandler {

    public static ResponseCreator getClientIdType(Model model, String LoginData, String Password) {

        try {
            String jsonString = model.getClientIdType(LoginData, Password);

            System.out.println(jsonString);

            return CustomResponse.ok(jsonString);
        } catch (Exception e) {
            return CustomResponse.error(401, e.getMessage());
        }
    }
}
