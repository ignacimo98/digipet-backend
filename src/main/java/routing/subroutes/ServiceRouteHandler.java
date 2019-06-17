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
            post("", map((req, res) -> ServiceHandler.insertService(model, req.body())));
            get("/:id", map((req, res) -> ServiceHandler.getService(model, Integer.parseInt(req.params(":id")))));
            post("/:id/report", map((req, res) -> ServiceHandler.updateReport(model, Integer.parseInt(req.params(":id")), req.body())));


        });

    }
}