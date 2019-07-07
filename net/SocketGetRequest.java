package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketGetRequest {

    public static void main(String[] args) throws IOException {

        try (var socket = new Socket("webcode.me", 80)) {

//            socket.setSoTimeout(5000);

            try (var wtr = new PrintWriter(socket.getOutputStream())) {

                // create GET request
                wtr.print("GET / HTTP/1.1\r\n");
                wtr.print("Host: www.webcode.me\r\n");
                wtr.print("\r\n");
                wtr.flush();
                socket.shutdownOutput();

                String outStr;

                try (var bufRead = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()))) {

                    while ((outStr = bufRead.readLine()) != null) {

                        System.out.println(outStr);
                    }

                    socket.shutdownInput();
                }
            }
        }
    }
}
