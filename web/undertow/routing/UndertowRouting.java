package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class UndertowRouting {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost", ROUTES)
                .build();

        server.start();
    }

    private static HttpHandler ROUTES = new RoutingHandler()
            .get("/", RoutingHandlers.plainTextHandler("GET - My Homepage"))
            .get("/about", RoutingHandlers.plainTextHandler("GET - about"))
            .post("/about", RoutingHandlers.plainTextHandler("POST - about"))
            .get("/new*", RoutingHandlers.plainTextHandler("GET - new*"))
            .setFallbackHandler(RoutingHandlers::notFoundHandler);
}
