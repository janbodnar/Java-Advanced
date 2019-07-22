package com.zetcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFileStream {

    public static void main(String[] args) throws IOException {

        var source = new File("src/resources/bugs.txt");
        var dest = new File("src/resources/bugs2.txt");

        try (var fis = new FileInputStream(source);
             var fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {

                fos.write(buffer, 0, length);
            }
        }
    }
}
