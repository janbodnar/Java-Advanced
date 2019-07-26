package com.zetcode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadImageFiles {

    public static void main(String[] args) throws IOException {

        var urlFile = "http://httpbin.org/image/jpeg";

        try(InputStream in = new URL(urlFile).openStream()){

            Files.copy(in, Paths.get("jackal.jpg"));
        }
    }
}
