package routing;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

public final class TokenFilter {

    public final static void apply() {
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                if (!(request.pathInfo() == "/login" ||
                        request.pathInfo() == "/signup/clients" ||
                        request.pathInfo() =="/signup/students")){ // Jimenaaaaa, aquí no sé si es .pathInfo(), .servletPath() o .contextPath()

                }
            }
        };
        Spark.after(filter);
    }
}
