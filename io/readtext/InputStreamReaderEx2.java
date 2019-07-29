package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputStreamReaderEx2 {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";
        var filePath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            String line;

            while ((line = br.readLine()) != null) {

                System.out.println(line);
            }
        }
    }
}
