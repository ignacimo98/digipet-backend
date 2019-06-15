package routing;

import dataobjects.Model;
import routing.subroutes.AdminRouteHandler;
import routing.subroutes.ClientRouteHandler;
import routing.subroutes.GenericRouteHandler;
import routing.subroutes.PetRouteHandler;

import java.util.ArrayList;


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


    }

    public void init(){
        for (GenericRouteHandler routeHandler: routeHandlers) {
            routeHandler.init();
        }
    }
}