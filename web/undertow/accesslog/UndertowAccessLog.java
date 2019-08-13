package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.DefaultAccessLogReceiver;
import io.undertow.util.Headers;

import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello there");
    }
}

public class UndertowAccessLog {

    public static void main(String[] args) {

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        DefaultAccessLogReceiver.Builder builder = DefaultAccessLogReceiver.builder().setLogWriteExecutor(executor)
                .setOutputDirectory(Paths.get("C:/Users/Jano/tmp/"))
                .setLogBaseName("access-log.")
                .setLogNameSuffix("log")
                .setRotate(true);

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(new AccessLogHandler(new MyHandler(), builder.build(),
                        "combined", UndertowAccessLog.class.getClassLoader()))
                .build();

        server.start();
    }
}
