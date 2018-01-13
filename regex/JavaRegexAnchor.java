package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Anchors match positions of characters inside a given text. 

public class JavaRegexAnchor {

    public static void main(String[] args) {

        List<String> sentences = Arrays.asList("I am looking for Jane.",
                "Jane was walking along the rive.",
                "Kate and Jane are close friends.");

        Pattern p = Pattern.compile("^Jane");

        for (String word : sentences) {

            Matcher m = p.matcher(word);

            if (m.find()) {
                System.out.printf("%s matches%n", word);
            } else {
                System.out.printf("%s does not match%n", word);
            }
        }
    }
}
