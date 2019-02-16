package com.zetcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class CountingWords {

    public static void main(String[] args) throws IOException {

        var wordCount = new HashMap<String, Integer>();

        var fileName = "src/resources/thermopylae.txt";

        var lines = Files.readAllLines(Paths.get(fileName),
                StandardCharsets.UTF_8);

        for (String line : lines) {

            var words = line.split("\\s+");

            for (String word : words) {

                if (word.endsWith(".") || word.endsWith(",")) {
                    word = word.substring(0, word.length()-1);
                }

                if (wordCount.containsKey(word)) {
                    wordCount.put(word, wordCount.get(word) + 1);

                } else {
                    wordCount.put(word, 1);
                }
            }
        }

        for (String key : wordCount.keySet()) {
            System.out.println(key + ": " + wordCount.get(key));
        }
    }
}
