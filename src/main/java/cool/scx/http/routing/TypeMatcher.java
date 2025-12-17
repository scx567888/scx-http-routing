package cool.scx.http.routing;

import dev.scx.http.ScxHttpServerRequest;

public interface TypeMatcher {

    TypeMatcher ANY = _ -> true;

    static TypeMatcher any() {
        return ANY;
    }

    boolean matches(ScxHttpServerRequest request);

}
