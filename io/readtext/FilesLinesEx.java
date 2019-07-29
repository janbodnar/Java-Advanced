package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesLinesEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";

        Files.lines(Paths.get(fileName)).forEachOrdered(System.out::println);
    }
}
