package routing.subroutes;


import routing.ResponseCreator;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

public abstract class GenericRouteHandler implements SparkApplication {

    abstract public void init();

    public Route map(Converter c) {
        return (req, res) -> c.convert(req, res).handle(req,res);
    }

    protected interface Converter {
        public ResponseCreator convert(Request req, Response res);
    }
}
