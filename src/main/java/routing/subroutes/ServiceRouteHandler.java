package routing.subroutes;

import dataobjects.Model;
import requesthandlers.LoginHandler;
import requesthandlers.ServiceHandler;
import requesthandlers.StudentHandler;

import static spark.Spark.*;

public class ServiceRouteHandler extends GenericRouteHandler {

    private final Model model;

    public ServiceRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/services", () -> {
            post("/availability", map((req, res) -> ServiceHandler.getCaregiverAvailability(model, Integer.parseInt(req.queryParams("idPet")), Integer.parseInt(req.queryParams("idPetOwner")),req.queryParams("startTime"), req.queryParams("endTime"), req.queryParams("location"))));
            post("", map((req, res) -> ServiceHandler.insertService(model, req.body())));
            get("/:id", map((req, res) -> ServiceHandler.getService(model, Integer.parseInt(req.params(":id")))));


        });

    }
}