package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import static io.undertow.Handlers.path;

class ItemHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");

        var value = exchange.getQueryParameters().get("name");

        String msg;

        if (value == null) {

            msg = "Hello there";
        } else {

            msg = String.format("Hello %s", value.getFirst());
        }

        exchange.getResponseSender().send(msg);
    }
}

public class UndertowQueryParam {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(path().addPrefixPath("/greet", new ItemHandler()))
                .build();
        server.start();
    }
}
