package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CopyFileStream2 {

    public static void main(String[] args) throws IOException {

        var source = Paths.get("src/resources/bugs.txt");
        var dest = Paths.get("src/resources/bugs2.txt");

        try (var fis = Files.newInputStream(source);
             var fos = Files.newOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {

                fos.write(buffer, 0, length);
            }
        }
    }
}
