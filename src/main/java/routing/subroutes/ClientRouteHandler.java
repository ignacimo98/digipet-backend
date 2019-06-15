package routing.subroutes;

import dataobjects.Model;

public class ClientRouteHandler extends GenericRouteHandler{

    private final Model model;

    public ClientRouteHandler(Model model) {
        this.model = model;
    }

    @Override
    public void init() {

    }
}
