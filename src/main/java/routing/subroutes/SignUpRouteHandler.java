package routing.subroutes;

import dataobjects.Model;
import requesthandlers.AdministratorHandler;
import requesthandlers.SignUpHandler;

import static spark.Spark.*;

public class SignUpRouteHandler extends GenericRouteHandler {

    private final Model model;

    public SignUpRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/signup", () -> {
            post("/clients", map((req, res) -> SignUpHandler.insertPetOwner(model, req.body())));

            post("/student", map((req, res) -> SignUpHandler.insertCaregiver(model,req.body())));

        });

    }
}