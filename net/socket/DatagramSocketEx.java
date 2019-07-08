package com.zetcode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// DatagramSocket provides network communication via UDP protocol
// The Quote of the Day (QOTD) service is a member of the Internet protocol
// suite, defined in RFC 865

public class DatagramSocketEx {

    public static void main(String[] args) throws IOException {

        var hostname = "djxmmx.net";
        int port = 17;

        var address = InetAddress.getByName(hostname);

        try (var socket = new DatagramSocket()) {

            var request = new DatagramPacket(new byte[1], 1, address, port);
            socket.send(request);

            var buffer = new byte[512];
            var response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            var quote = new String(buffer, 0, response.getLength());

            System.out.println(quote);
        }
    }
}
