package com.zetcode;

import org.eclipse.jetty.server.Server;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        var server = new Server(8080);

        server.setHandler(new MyHandler());
        server.start();
        server.join();
    }
}
