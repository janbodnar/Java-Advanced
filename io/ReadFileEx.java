package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFileEx {

    public static void main(String[] args) throws IOException {

        var path = Paths.get("src/resources/thermopylae.txt");

        var content = Files.readString(path);

        System.out.println(content);
    }
}
