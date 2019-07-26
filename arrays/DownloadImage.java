package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadImage {

    public static void main(String[] args) throws IOException {

        var imageUrl = "http://webcode.me/favicon.ico";
        var destinationFile = "favicon.ico";

        var url = new URL(imageUrl);

        try (var is = url.openStream();
             var fos = new FileOutputStream(destinationFile)) {

            byte[] b = new byte[1024];
            int noOfBytes;

            while ((noOfBytes = is.read(b)) != -1) {

                fos.write(b, 0, noOfBytes);
            }
        }
    }
}
