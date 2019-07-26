package com.zetcode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyImage {

    public static void main(String[] args) throws IOException {

        var sourceFile = "src/resources/sid.jpg";
        var destFile = "src/resources/sid2.jpg";

        try (var fis = new FileInputStream(sourceFile);
             var fos = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];
            int noOfBytes = 0;

            // read bytes from source file and write to destination file
            while ((noOfBytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, noOfBytes);
            }
        }
    }
}
