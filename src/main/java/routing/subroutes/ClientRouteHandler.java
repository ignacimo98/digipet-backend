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
            get("/:id", map((req, res) -> ClientHandler.getPetOwner(model, Integer.parseInt(req.params(":id")))));
            get("/:id/services", map((req, res) -> ClientHandler.getServices(model, Integer.parseInt(req.params(":id")))));

        });

    }
}
