package cool.scx.http.routing.test;

import cool.scx.http.routing.Router;
import dev.scx.http.exception.UnauthorizedException;

import java.io.IOException;

import static dev.scx.http.method.HttpMethod.GET;
import static dev.scx.http.method.HttpMethod.POST;

// todo 待改造
public class Test {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        var l = System.nanoTime();
//        var server = new HttpServer();

        var router = Router.of();

//        router.route(-100000).handler(new CorsHandler().addOrigin("http://localhost:18899"));

        router.route().path("/abc").handler(c -> {
            var bytes = c.request().body().asBytes();
            System.out.println(bytes.length);
//            var bbb=c.request().body().asMultiPart();
//            for (var aByte : bbb) {
//                System.out.println(aByte.name()+" "+wrap(()-> aByte.inputStream().readAllBytes().length));
//            }
            c.response().send("12312313");
//            c.next();
        });

        router.route().path("/*").handler(c -> {
            System.out.println(c.request().path());
            c.next();
        });

        router.route().path("/hello").method(GET).handler(c -> {
            c.response().send("hello");
        });

        router.route().path("/path-params/:id").method(GET).handler(c -> {
            c.response().send("id : " + c.pathParams().get("id"));
        });

        router.route().path("/401").method(GET).handler(c -> {
            throw new UnauthorizedException();
        });

        router.route().path("/405").method(POST).handler(c -> {
            System.out.println("405");
        });

        router.route().path("/last").method(GET).handler(c -> {
            var r = 1 / 0;
        });

//        server.onRequest(router);

//        server.start(8080);

        System.out.println("HttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
    }

}
