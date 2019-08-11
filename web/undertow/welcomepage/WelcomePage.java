package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;

// src/main/resources/public/index.html

public class WelcomePage {

    public static void main(String[] args) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(path().addPrefixPath("/",
                        resource(new ClassPathResourceManager(WelcomePage.class.getClassLoader()))
                                .addWelcomeFiles("public/index.html")))
                .build();

        server.start();
    }
}

