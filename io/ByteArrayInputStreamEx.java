package com.zetcode;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteArrayInputStreamEx {

    public static void main(String[] args) throws IOException {

        var text = "There is a golden eagle high in the sky";

        byte[] data = text.getBytes();

        try (var bais = new ByteArrayInputStream(data)) {

            int len = bais.available();
            System.out.printf("There are %d bytes%n", len);

            int c;
            while ((c = bais.read()) != -1) {

                System.out.printf("%c", (char) c);
            }

            System.out.println();
        }
    }
}
