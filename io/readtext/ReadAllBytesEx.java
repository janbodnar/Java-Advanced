package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadAllBytesEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";
        var filePath = Paths.get(fileName);

        byte[] data = Files.readAllBytes(filePath);
        var content = new String(data);

        System.out.println(content);
    }
}
