package routing;

import dataobjects.Model;
import routing.subroutes.AdminRoutesHandler;


public class Application {

    private final Model model;
    private AdminRoutesHandler adminRoutesHandler;

    public Application(Model model) {
        this.model = model;
        adminRoutesHandler = new AdminRoutesHandler(model);
    }

    public void init(){
        adminRoutesHandler.init();
    }

}