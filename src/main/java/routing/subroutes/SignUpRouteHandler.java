package routing.subroutes;

import dataobjects.Model;
import requesthandlers.AdministratorHandler;

import static spark.Spark.*;

public class SignUpRouteHandler extends GenericRouteHandler {

    private final Model model;

    public SignUpRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/signup", () -> {
            //get("/client", map((req, res) -> SignUpHandler.insertPetOwner(model)));
            //post("/student", map((req, res) -> SignUpHandler.insertCaregiver(model)));

        });

    }
}