package routing.subroutes;

import dataobjects.Model;
import requesthandlers.AdministratorHandler;
import requesthandlers.LoginHandler;

import static spark.Spark.*;

public class LoginRouteHandler extends GenericRouteHandler {

    private final Model model;

    public LoginRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/login", () -> {
            get("", map((req, res) -> LoginHandler.getClientIdType(model, req.queryParams("user"), req.queryParams("password"))));

        });

    }
}