package com.zetcode;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class MyHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");

        try (var bis = new BufferedReader(new InputStreamReader(exchange.getInputStream(),
                StandardCharsets.UTF_8))) {

            var buffer = new StringBuilder();

            bis.lines().forEach(buffer::append);

            exchange.getResponseSender().send(buffer.toString());
        }
    }
}

public class UndertowJsonEx {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(Handlers.pathTemplate()
                        .add("/data", new BlockingHandler(new MyHandler()))
                )
                .build();
        server.start();
    }
}
