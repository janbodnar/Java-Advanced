package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import static io.undertow.Handlers.path;


class MyHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {

        var hostName = exchange.getHostName();
        var port = exchange.getHostPort();
        var method = exchange.getRequestMethod();
        var protocol = exchange.getProtocol();
        var scheme = exchange.getRequestScheme();
        var path = exchange.getRequestPath();

        var output = String.format("host: %s%n port: %s%n method: %s%n protocol: %s%n scheme: %s%n path: %s%n",
                hostName, port, method, protocol, scheme, path);

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(output);
    }
}

public class UndertowRequestInfo {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(path().addPrefixPath("/", new MyHandler()))
                .build();

        server.start();
    }
}
