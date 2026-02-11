package dev.scx.http.routing;

import dev.scx.function.Function1Void;
import dev.scx.http.ScxHttpServerRequest;

import java.util.List;

/// Router
///
/// @author scx567888
/// @version 0.0.1
public interface Router extends Function1Void<ScxHttpServerRequest, Throwable> {

    static Router of() {
        return new RouterImpl();
    }

    Router addRoute(Route route);

    List<Route> getRoutes();

    default RouteWritable route() {
        var route = Route.of();
        addRoute(route);
        return route;
    }

    default RouteWritable route(int order) {
        var route = Route.of().order(order);
        addRoute(route);
        return route;
    }

}
