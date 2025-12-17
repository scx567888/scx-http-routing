package cool.scx.http.routing.test;

import cool.scx.http.routing.PathMatcher;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PathMatcherTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var matcher = PathMatcher.of("/a/b/:id");
        var result = matcher.matches("/a/b/苹果");
        Assert.assertTrue(result.accepted());
        Assert.assertEquals(result.pathParams().get("id"), "苹果");

        var result1 = matcher.matches("/a/b/ 空格 ");
        Assert.assertTrue(result1.accepted());
        Assert.assertEquals(result1.pathParams().get("id"), " 空格 ");

        var result2 = matcher.matches("/a/b/c/ ");
        Assert.assertFalse(result2.accepted());

        var matcher2 = PathMatcher.of("/a/b/*");

        var result5 = matcher2.matches("/a/b/");
        Assert.assertTrue(result5.accepted());
        Assert.assertEquals(result5.pathParams().get("*"), "");

        var result6 = matcher2.matches("/a/b/c/d/f/e/f");
        Assert.assertTrue(result6.accepted());
        Assert.assertEquals(result6.pathParams().get("*"), "c/d/f/e/f");

        var matcher3 = PathMatcher.of("/a/b/:name/*");

        var result7 = matcher3.matches("/a/b/小明/");
        Assert.assertTrue(result7.accepted());
        Assert.assertEquals(result7.pathParams().get("name"), "小明");
        Assert.assertEquals(result7.pathParams().get("*"), "");

        var result8 = matcher3.matches("/a/b/小明/9");
        Assert.assertTrue(result8.accepted());
        Assert.assertEquals(result8.pathParams().get("name"), "小明");
        Assert.assertEquals(result8.pathParams().get("*"), "9");
    }

}
