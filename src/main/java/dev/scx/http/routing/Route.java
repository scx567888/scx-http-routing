package dev.scx.http.routing;

import dev.scx.function.Function1Void;
import dev.scx.http.method.HttpMethod;

import java.util.Set;

/// Route 路由只保存状态不做行为处理
///
/// @author scx567888
/// @version 0.0.1
public interface Route {

    static RouteWritable of() {
        return new RouteImpl();
    }

    String path();

    Set<HttpMethod> methods();

    TypeMatcher typeMatcher();

    PathMatcher pathMatcher();

    MethodMatcher methodMatcher();

    int order();

    Function1Void<RoutingContext, ?> handler();

}
