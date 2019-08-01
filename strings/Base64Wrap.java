package com.zetcode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Wrap {

    public static void main(String[] args) throws IOException {

        var src = "Golden eagle in the sky";

        try (var os = Base64.getEncoder().wrap(new FileOutputStream("src/resources/base64.txt"))) {

            os.write(src.getBytes(StandardCharsets.UTF_8));
        }

        try (var is = Base64.getDecoder().wrap(new FileInputStream("src/resources/base64.txt"))) {

            int len;
            byte[] bytes = new byte[256];
            while ((len = is.read(bytes)) != -1) {

                System.out.print(new String(bytes, 0, len, StandardCharsets.UTF_8));
            }
        }
    }
}
