package cool.scx.http.routing;

import dev.scx.http.ScxHttpServerRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/// Router
///
/// @author scx567888
/// @version 0.0.1
public class RouterImpl implements Router {

    private static final Comparator<Route> ROUTE_COMPARATOR = (o1, o2) -> {
        var compare = Integer.compare(o1.order(), o2.order());
        if (compare == 0) {
            if (o1.equals(o2)) {
                return 0;
            }
            return 1;
        }
        return compare;
    };

    final TreeSet<Route> routes;

    public RouterImpl() {
        this.routes = new TreeSet<>(ROUTE_COMPARATOR);
    }

    @Override
    public RouterImpl addRoute(Route route) {
        routes.add(route);
        return this;
    }

    @Override
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public void apply(ScxHttpServerRequest scxHttpRequest) throws Throwable {
        new RoutingContextImpl(this, scxHttpRequest).next();
    }

}
