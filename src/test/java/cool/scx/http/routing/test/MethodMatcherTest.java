package cool.scx.http.routing.test;

import cool.scx.http.routing.MethodMatcher;
import dev.scx.http.method.ScxHttpMethod;

import static dev.scx.http.method.HttpMethod.GET;
import static dev.scx.http.method.HttpMethod.POST;

public class MethodMatcherTest {

    public static void main(String[] args) {
        var matcher = MethodMatcher.of(GET, POST);
        var result = matcher.matches(ScxHttpMethod.of("GET"));
        System.out.println(result);
    }

}
