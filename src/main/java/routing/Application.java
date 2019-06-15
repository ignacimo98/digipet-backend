package routing;

import dataobjects.Administrator;
import dataobjects.Model;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application implements SparkApplication {

    private final Model model;

    public Application(Model model) {
        this.model = model;
    }

    @Override
    public void init() {
        get("/admins", map((req, res)-> Administrator.getAllAdmins(model)));
        post("/administrators", map((req, res)-> Administrator.insertAdmin(model, req.body())));

    }

    Route map(Converter c) {
        return (req, res) -> c.convert(req, res).handle(req,res);
    }

    private interface Converter {
        public ResponseCreator convert(Request req, Response res);
    }
}