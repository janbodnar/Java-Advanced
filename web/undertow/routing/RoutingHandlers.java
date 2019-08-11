package com.zetcode;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RoutingHandlers {

    public static HttpHandler plainTextHandler(String value) {
    
        return new PlainTextHandler(value);
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
    
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Page Not Found");
    }
}
