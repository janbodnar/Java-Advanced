package com.zetcode;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UndertowSendImage {

    public static void main(String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(Handlers.path().addExactPath("/image",
                        new BlockingHandler(exchange -> {
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "image/jpeg");
//                    exchange.startBlocking();
                            writeToOutputStream(exchange.getOutputStream());
                        }))).build();
        server.start();
    }

    private static void writeToOutputStream(OutputStream os) throws IOException, InterruptedException {

        var fileName = "src/main/resources/sid.jpg";

        byte[] buf = new byte[1024];

        try (var fis = new FileInputStream(fileName)) {

            int nfOfBytes;

            while ((nfOfBytes = fis.read(buf, 0, buf.length)) != -1) {
                os.write(buf, 0, nfOfBytes);
            }
        }

        Thread.sleep(5000);
    }
}
