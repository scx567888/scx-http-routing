package cool.scx.http.routing;

import dev.scx.function.Function1Void;
import dev.scx.http.method.HttpMethod;

import java.util.HashSet;
import java.util.Set;

/// RouteImpl 路由只保存状态不做行为处理
///
/// @author scx567888
/// @version 0.0.1
class RouteImpl implements RouteWritable {

    private String path;
    private Set<HttpMethod> methods;
    private TypeMatcher typeMatcher;
    private PathMatcher pathMatcher;
    private MethodMatcher methodMatcher;
    private int order;
    private Function1Void<RoutingContext, ?> handler;

    public RouteImpl() {
        this.path = null;
        this.methods = new HashSet<>();
        this.typeMatcher = TypeMatcher.any();
        this.pathMatcher = PathMatcher.any();
        this.methodMatcher = MethodMatcher.any();
        this.order = 0;
        this.handler = RoutingContext::next;
    }

    @Override
    public RouteWritable type(TypeMatcher type) {
        this.typeMatcher = type;
        return this;
    }

    @Override
    public RouteImpl path(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.of(path);
        return this;
    }

    @Override
    public RouteImpl pathRegex(String path) {
        this.path = path;
        this.pathMatcher = PathMatcher.ofRegex(path);
        return this;
    }

    @Override
    public RouteImpl method(HttpMethod... httpMethods) {
        this.methods = Set.of(httpMethods);
        this.methodMatcher = MethodMatcher.of(httpMethods);
        return this;
    }

    @Override
    public RouteImpl order(int order) {
        this.order = order;
        return this;
    }

    @Override
    public RouteImpl handler(Function1Void<RoutingContext, ?> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Set<HttpMethod> methods() {
        return methods;
    }

    @Override
    public TypeMatcher typeMatcher() {
        return typeMatcher;
    }

    @Override
    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    @Override
    public MethodMatcher methodMatcher() {
        return methodMatcher;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public Function1Void<RoutingContext, ?> handler() {
        return handler;
    }

}
