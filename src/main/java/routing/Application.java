package routing;

import dataobjects.Model;
import routing.subroutes.*;

import java.util.ArrayList;


public class Application {

    private final Model model;
    private ArrayList<GenericRouteHandler> routeHandlers;
    private AdminRouteHandler adminRouteHandler;

    public Application(Model model) {
        this.model = model;
        CorsFilter.apply();
        routeHandlers = new ArrayList<>();
        routeHandlers.add(new AdminRouteHandler(model));
        routeHandlers.add(new PetRouteHandler(model));
        routeHandlers.add(new ClientRouteHandler(model));
        routeHandlers.add(new LoginRouteHandler(model));


    }

    public void init(){
        for (GenericRouteHandler routeHandler: routeHandlers) {
            routeHandler.init();
        }
    }
}