package dev.scx.http.routing;

import dev.scx.http.method.ScxHttpMethod;

import java.util.Set;

/// MethodMatcherImpl
///
/// @author scx567888
/// @version 0.0.1
class MethodMatcherImpl implements MethodMatcher {

    private final Set<ScxHttpMethod> methods;

    MethodMatcherImpl(ScxHttpMethod... methods) {
        this.methods = Set.of(methods);
    }

    @Override
    public boolean matches(ScxHttpMethod method) {
        return methods.contains(method);
    }

}
