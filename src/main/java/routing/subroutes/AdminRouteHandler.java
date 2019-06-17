package routing.subroutes;

import dataobjects.Administrator;
import dataobjects.Model;
import requesthandlers.AdministratorHandler;

import static spark.Spark.*;

public class AdminRouteHandler extends GenericRouteHandler {

    private final Model model;

    public AdminRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path("/administrators", () -> {
            get("", map((req, res) -> AdministratorHandler.getAdmin(model, Integer.parseInt(req.attribute("id").toString()))));
            post("", map((req, res) -> AdministratorHandler.insertAdmin(model, req.body())));
            post("/block/:id", map(((req, res) -> AdministratorHandler.blockCaregiver(model, Integer.parseInt(req.params(":id"))))));
            get("/complaints", map((req, res) -> AdministratorHandler.getComplaints(model)));
            post("/report", map((req, res) -> AdministratorHandler.getReport(model, req.body())));






        });

    }
}
