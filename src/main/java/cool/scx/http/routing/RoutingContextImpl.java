package cool.scx.http.routing;

import dev.scx.http.ScxHttpServerRequest;
import dev.scx.http.ScxHttpServerResponse;
import dev.scx.http.exception.HttpException;
import dev.scx.http.exception.MethodNotAllowedException;
import dev.scx.http.exception.NotFoundException;
import dev.scx.http.parameters.Parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/// RoutingContext
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public class RoutingContextImpl implements RoutingContext {

    private final RouterImpl router;
    private final ScxHttpServerRequest request;
    private final Iterator<Route> iter;
    private final Map<String, Object> data;
    private Parameters<String, String> nowPathParams;

    RoutingContextImpl(RouterImpl router, ScxHttpServerRequest request) {
        this.router = router;
        this.request = request;
        this.iter = router.routes.iterator();
        this.data = new HashMap<>();
    }

    @Override
    public <T extends ScxHttpServerRequest> T request() {
        return (T) request;
    }

    @Override
    public <T extends ScxHttpServerResponse> T response() {
        return (T) request.response();
    }

    @Override
    public void next() throws Throwable {
        HttpException e = new NotFoundException();

        while (iter.hasNext()) {
            var route = iter.next();

            //匹配类型
            var typeMatcherResult = route.typeMatcher().matches(request);

            if (!typeMatcherResult) {
                continue;
            }

            //匹配路径
            var pathMatchResult = route.pathMatcher().matches(request.path());

            this.nowPathParams = pathMatchResult.pathParams();

            //匹配不到就下一次
            if (!pathMatchResult.accepted()) {
                continue;
            }

            //匹配方法
            var methodMatchResult = route.methodMatcher().matches(request.method());

            //匹配方法失败
            if (!methodMatchResult) {
                e = new MethodNotAllowedException();
                continue;
            }

            route.handler().apply(this);

            return;

        }

        throw e;
    }

    @Override
    public Parameters<String, String> pathParams() {
        return this.nowPathParams;
    }

    @Override
    public <T> Map<String, T> data() {
        return (Map<String, T>) data;
    }

}
