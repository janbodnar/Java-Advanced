package com.zetcode;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;

import java.nio.file.Paths;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;

// Serving files from Documents/web/ directory located
// in user's home directory

public class UndertowFileServer {

    public static void main(String[] args) {

        var myPath = String.format("%s/Documents/web", System.getProperty("user.home"));

        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(path().addPrefixPath("/show", resource(new PathResourceManager(
                        Paths.get(myPath), 100)).setDirectoryListingEnabled(false)))
                        .build();
        server.start();
    }
}
