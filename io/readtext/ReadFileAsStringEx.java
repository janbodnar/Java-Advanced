package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFileAsStringEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";
        var filePath = Paths.get(fileName);

        var content = Files.readString(filePath);

        System.out.println(content);
    }
}
