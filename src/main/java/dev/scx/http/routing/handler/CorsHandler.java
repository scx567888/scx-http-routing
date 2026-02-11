package dev.scx.http.routing.handler;

import dev.scx.http.routing.RoutingContext;
import dev.scx.function.Function1Void;
import dev.scx.http.ScxHttpServerResponse;
import dev.scx.http.exception.ForbiddenException;
import dev.scx.http.headers.ScxHttpHeaderName;
import dev.scx.http.method.HttpMethod;
import dev.scx.http.method.ScxHttpMethod;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static dev.scx.http.headers.HttpHeaderName.*;
import static java.util.Collections.addAll;

/// CorsHandler
///
/// @author scx567888
/// @version 0.0.1
public class CorsHandler implements Function1Void<RoutingContext, Throwable> {

    private final Set<String> allowedMethods = new LinkedHashSet<>();
    private final Set<String> allowedHeaders = new LinkedHashSet<>();
    private final Set<String> exposedHeaders = new LinkedHashSet<>();
    private Set<String> origins;
    private String allowedMethodsString;
    private String allowedHeadersString;
    private String exposedHeadersString;
    private boolean allowCredentials;
    private String maxAgeSeconds;

    public CorsHandler() {
        origins = null;
    }

    private boolean starOrigin() {
        return origins == null;
    }

    public CorsHandler addOrigin(String origin) {
        Objects.requireNonNull(origin, "'origin' cannot be null");
        if (origin.equals("*")) {
            this.origins = null;
            return this;
        }
        if (origins == null) {
            origins = new LinkedHashSet<>();
        }
        origins.add(origin);
        return this;
    }

    public CorsHandler allowedMethod(String... methods) {
        addAll(allowedMethods, methods);
        allowedMethodsString = String.join(",", allowedMethods);
        return this;
    }

    public CorsHandler allowedHeader(String... headerNames) {
        addAll(allowedHeaders, headerNames);
        allowedHeadersString = String.join(",", allowedHeaders);
        return this;
    }

    public CorsHandler exposedHeader(String... headerNames) {
        addAll(exposedHeaders, headerNames);
        exposedHeadersString = String.join(",", exposedHeaders);
        return this;
    }

    public CorsHandler allowedMethod(ScxHttpMethod... methods) {
        return allowedMethod(Arrays.stream(methods).map(ScxHttpMethod::value).toArray(String[]::new));
    }

    public CorsHandler allowedHeader(ScxHttpHeaderName... headerNames) {
        return allowedHeader(Arrays.stream(headerNames).map(ScxHttpHeaderName::value).toArray(String[]::new));
    }

    public CorsHandler exposedHeader(ScxHttpHeaderName... headerNames) {
        return allowedHeader(Arrays.stream(headerNames).map(ScxHttpHeaderName::value).toArray(String[]::new));
    }

    public CorsHandler allowCredentials(boolean allow) {
        this.allowCredentials = allow;
        return this;
    }

    public CorsHandler maxAgeSeconds(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds == -1 ? null : String.valueOf(maxAgeSeconds);
        return this;
    }

    @Override
    public void apply(RoutingContext context) throws Throwable {
        var request = context.request();
        var response = context.response();
        var origin = context.request().getHeader(ORIGIN);
        if (origin == null) {
            // 不是 CORS 请求 - 什么都不做 直接 next
            context.next();
        } else if (isValidOrigin(origin)) {
            var accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);
            if (request.method() == HttpMethod.OPTIONS && accessControlRequestMethod != null) {
                // 预检 请求
                addCredentialsAndOriginHeader(response, origin);
                if (allowedMethodsString != null) {
                    response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allowedMethodsString);
                }
                if (allowedHeadersString != null) {
                    response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, allowedHeadersString);
                } else {
                    if (request.headers().contains(ACCESS_CONTROL_REQUEST_HEADERS)) {
                        // 回显请求标头
                        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS));
                    }
                }
                if (maxAgeSeconds != null) {
                    response.setHeader(ACCESS_CONTROL_MAX_AGE, maxAgeSeconds);
                }

                response.statusCode(204).send();

            } else {
                addCredentialsAndOriginHeader(response, origin);
                if (exposedHeadersString != null) {
                    response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, exposedHeadersString);
                }
                context.next();
            }
        } else {
            //向客户端报告 CORS 错误
            throw new ForbiddenException("CORS Rejected - Invalid origin");
        }
    }

    private void addCredentialsAndOriginHeader(ScxHttpServerResponse response, String origin) {
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            // Must be exact origin (not '*') in case of credentials
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        } else {
            // Can be '*' too
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, getAllowedOrigin(origin));
        }
    }

    private boolean isValidOrigin(String origin) {
        // "*" 的意思是全部 origin
        if (starOrigin()) {
            return true;
        }

        // 否则我们进行完全匹配
        for (var allowedOrigin : origins) {
            if (allowedOrigin.equals(origin)) {
                return true;
            }
        }

        return false;
    }

    private String getAllowedOrigin(String origin) {
        if (starOrigin()) {
            return "*";
        }
        return origin;
    }

}
