package com.zetcode;

import io.undertow.Undertow;
import io.undertow.util.Headers;

public class UndertowSimple {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello there");
                }).build();

        server.start();
    }
}
