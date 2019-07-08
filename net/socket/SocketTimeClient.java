package com.zetcode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTimeClient {

    public static void main(String[] args) {

        var hostname = "2.europe.pool.ntp.org";
        int port = 13;

        try (var socket = new Socket(hostname, port)) {

            try (var reader = new InputStreamReader(socket.getInputStream())) {

                int character;
                var output = new StringBuilder();

                while ((character = reader.read()) != -1) {
                    output.append((char) character);
                }

                System.out.println(output);
            }

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
