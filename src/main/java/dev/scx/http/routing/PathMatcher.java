package dev.scx.http.routing;

import dev.scx.http.parameters.Parameters;

/// PathMatcher
///
/// @author scx567888
/// @version 0.0.1
public interface PathMatcher {

    PathMatcher ANY = _ -> new MatchResult(true, Parameters.of());

    static PathMatcher any() {
        return ANY;
    }

    static PathMatcher of(String path) {
        var pathMatcher = new PathMatcherImpl();
        pathMatcher.checkPath(path);
        pathMatcher.setPath(path);
        return pathMatcher;
    }

    static PathMatcherImpl ofRegex(String regex) {
        var pathMatcher = new PathMatcherImpl();
        pathMatcher.setRegex(regex);
        return pathMatcher;
    }

    MatchResult matches(String path);

    record MatchResult(boolean accepted, Parameters<String, String> pathParams) {

    }

}
