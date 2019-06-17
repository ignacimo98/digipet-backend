package routing.subroutes;

import dataobjects.Model;
import requesthandlers.StudentHandler;

import static spark.Spark.*;

public class StudentRouteHandler extends GenericRouteHandler{

    private final Model model;

    public StudentRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/students", () -> {
            get("", map((req, res) -> StudentHandler.getCaregiver(model, Integer.parseInt(req.attribute("id").toString()))));
            get("/:id", map((req, res) -> StudentHandler.getCaregiver(model, Integer.parseInt(req.params(":id")))));
            get("/:id/services", map((req, res) -> StudentHandler.getServices(model, Integer.parseInt(req.params(":id")))));
            post("/:id/schedule", map((req, res)-> StudentHandler.insertSchedule(model, req.body())));
            post("/:id/changestatus", map((req, res) -> StudentHandler.changeCaregiverStatus(model, Integer.parseInt(req.params(":id")), req.body())));


        });

    }
}
