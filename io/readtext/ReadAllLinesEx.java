package com.zetcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadAllLinesEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";

        List<String> lines = Files.readAllLines(Paths.get(fileName),
                StandardCharsets.UTF_8);

        for (String line : lines) {

            System.out.println(line);
        }
    }
}
