package com.zetcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.LocalDate;

public class DateServer {

    public static void main(String[] args) throws IOException {

        int port = 8081;

        try (var listener = new ServerSocket(port)) {

            System.out.printf("The started on port %d%n", port);

            while (true) {

                try (var socket = listener.accept()) {

                    try (var pw = new PrintWriter(socket.getOutputStream(), true)) {

                        pw.println(LocalDate.now());
                    }
                }
            }
        }
    }
}
