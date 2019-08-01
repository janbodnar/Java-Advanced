package com.zetcode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64MimeEncoder {

    public static void main(String[] args) throws IOException {

        try (var fis = new FileInputStream("src/resources/favicon.ico")) {

            byte[] bytes = fis.readAllBytes();
            var encodedString = Base64.getMimeEncoder().encodeToString(bytes);

            System.out.println(encodedString);
        }
    }
}
