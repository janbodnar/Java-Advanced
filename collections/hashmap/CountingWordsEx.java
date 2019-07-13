package com.zetcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountingWordsEx {

    public static void main(String[] args) throws IOException {

        Map<String, Integer> wordCount = new HashMap<>();

        String fileName = "src/resources/thermopylae.txt";
        
        List<String> lines = Files.readAllLines(Paths.get(fileName),
                StandardCharsets.UTF_8);

        for (String line : lines) {

            String[] words = line.split("\\s+");

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
