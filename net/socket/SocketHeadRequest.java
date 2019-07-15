package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHeadRequest {

    public static void main(String[] args) throws IOException {

        var hostname = "webcode.me";
        int port = 80;

        try (var socket = new Socket(hostname, port)) {

            try (var writer = new PrintWriter(socket.getOutputStream(), true)) {

                writer.println("HEAD / HTTP/1.1");
                writer.println("Host: " + hostname);
                writer.println("User-Agent: Console Http Client");
                writer.println("Accept: text/html");
                writer.println("Accept-Language: en-US");
                writer.println("Connection: close");
                writer.println();

                try (var reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        System.out.println(line);
                    }
                }
            }
        }
    }
}
