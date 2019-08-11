package com.zetcode;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.PathTemplateMatch;

class ItemHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");

        // Method 1
//        PathTemplateMatch pathMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
//        String itemId = pathMatch.getParameters().get("itemId");

        // Method 2
        String itemId = exchange.getQueryParameters().get("itemId").getFirst();

        var msg = String.format("Received Item Id %s", itemId);

        exchange.getResponseSender().send(msg);
    }
}

public class UndertowPathParam {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(Handlers.pathTemplate()
                        .add("/item/{itemId}", new ItemHandler())
                )
                .build();
        server.start();
    }
}
