package com.zetcode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Reading a file with getResource()

public class JavaReadResourceEx2 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var filePath = "resources/words.txt";

        var uri = JavaReadResourceEx2.class.getClassLoader()
                .getResource(filePath).toURI();

        var mainPath = Paths.get(uri).toString();

        var lines = Files.readAllLines(Paths.get(mainPath));
        lines.stream().forEach(System.out::println);
    }
}
