package com.zetcode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// time servers come and go; we might need to 
// find a functioning server on https://www.ntppool.org/en/
public class SocketTimeClient {

    public static void main(String[] args) throws IOException {

        var hostname = "3.se.pool.ntp.org";
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
        }
    }
}
