package routing.subroutes;

import dataobjects.Model;
import requesthandlers.AdministratorHandler;
import requesthandlers.PetHandler;

import static spark.Spark.*;

public class PetRouteHandler extends GenericRouteHandler{

    private final Model model;

    public PetRouteHandler(Model model) {
        this.model = model;
    }
    @Override
    public void init() {
        path("/pets", () -> {
            get("/:id", map((req, res) -> PetHandler.getPet(model, Integer.parseInt(req.params(":id")))));
            get("/new", map((req, res) -> PetHandler.insertPet(model, req.body())));

        });
    }
}
