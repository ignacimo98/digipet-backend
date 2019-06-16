package routing;

import dataobjects.Model;
import routing.subroutes.*;

import java.util.ArrayList;

import static spark.Spark.options;


public class Application {

    private final Model model;
    private ArrayList<GenericRouteHandler> routeHandlers;
    private AdminRouteHandler adminRouteHandler;

    public Application(Model model) {
        this.model = model;
        routeHandlers = new ArrayList<>();
        routeHandlers.add(new AdminRouteHandler(model));
        routeHandlers.add(new PetRouteHandler(model));
        routeHandlers.add(new ClientRouteHandler(model));
        routeHandlers.add(new LoginRouteHandler(model));
        routeHandlers.add(new SignUpRouteHandler(model));
        routeHandlers.add(new StudentRouteHandler(model));
        routeHandlers.add(new ServiceRouteHandler(model));


    }

    public void init(){
        CorsFilter.apply();
        TokenFilter.apply();
        options("*", (req, res) -> "{}");
        for (GenericRouteHandler routeHandler: routeHandlers) {
            routeHandler.init();
        }

    }
}