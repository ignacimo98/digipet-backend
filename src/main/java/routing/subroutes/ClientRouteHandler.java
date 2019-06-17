package routing.subroutes;

import dataobjects.Model;
import requesthandlers.ClientHandler;

import static spark.Spark.*;

public class ClientRouteHandler extends GenericRouteHandler{

    private final Model model;

    public ClientRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/clients", () -> {
            get("", map((req, res) -> ClientHandler.getPetOwner(model, Integer.parseInt(req.attribute("id").toString()))));
            get("/:id", map((req, res) -> ClientHandler.getPetOwner(model, Integer.parseInt(req.params(":id")))));
            get("/:id/services", map((req, res) -> ClientHandler.getServices(model, Integer.parseInt(req.params(":id")))));
            get("/:id/pets", map((req, res) -> ClientHandler.getPets(model, Integer.parseInt(req.params(":id")))));

        });

    }
}
