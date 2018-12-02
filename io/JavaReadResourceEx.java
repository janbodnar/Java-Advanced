package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaReadResourceEx {

    public static void main(String[] args) throws IOException  {

        var fileName = "src/resources/words.txt";

        var lines = Files.readAllLines(Paths.get(fileName));

        lines.stream().forEach(System.out::println);

//        Files.lines(Paths.get(fileName)).forEach(System.out::println);
    }
}
