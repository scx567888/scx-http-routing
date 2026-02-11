package dev.scx.http.routing;

import dev.scx.http.method.ScxHttpMethod;

/// MethodMatcher
///
/// @author scx567888
/// @version 0.0.1
public interface MethodMatcher {

    MethodMatcher ANY = _ -> true;

    static MethodMatcher any() {
        return ANY;
    }

    static MethodMatcher of(ScxHttpMethod... method) {
        return new MethodMatcherImpl(method);
    }

    boolean matches(ScxHttpMethod method);

}
