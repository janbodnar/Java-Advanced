package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// An array of bytes is used to store data 
// read from a file
public class ReadAllBytesEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";
        var filePath = Paths.get(fileName);

        byte[] data = Files.readAllBytes(filePath);
        var content = new String(data);

        System.out.println(content);
    }
}
