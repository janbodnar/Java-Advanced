package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// probing whois.iana.org might give the right
// whois server

public class WhoisClientEx {

    public static void main(String[] args) {

        var domainName = "webcode.me";
        var whoisServer = "whois.nic.me";
        int port = 43;

        try (var socket = new Socket(whoisServer, port)) {

            try (var writer = new PrintWriter(socket.getOutputStream(), true)) {

                writer.println(domainName);

                try (var reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        System.out.println(line);
                    }
                }
            }
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
