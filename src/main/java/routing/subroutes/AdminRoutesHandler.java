package routing.subroutes;


import dataobjects.Model;
import requesthandlers.AdministratorHandler;
import routing.ResponseCreator;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import static spark.Spark.*;

public class AdminRoutesHandler implements SparkApplication {

    private final Model model;

    public AdminRoutesHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        path( "/administrators", () -> {
            get("/", map((req, res) -> AdministratorHandler.getAllAdmins(model)));
            post("/", map((req, res) -> AdministratorHandler.insertAdmin(model, req.body())));

        });

    }

    Route map(Converter c) {
        return (req, res) -> c.convert(req, res).handle(req,res);
    }

    private interface Converter {
        public ResponseCreator convert(Request req, Response res);
    }
}