package com.zetcode;

// With () characters we have subpatterns

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexSubpatterns {

    public static void main(String[] args) {
        
        List<String> words = Arrays.asList("book", "bookshelf", "bookworm",
                "bookcase", "bookish", "bookkeeper", "booklet", "bookmark");

        Pattern p = Pattern.compile("book(worm|worm|keeper)?");

        for (String word : words) {

            Matcher m = p.matcher(word);

            if (m.matches()) {
                System.out.printf("%s matches%n", word);
            } else {
                System.out.printf("%s does not match%n", word);
            }
        }        
    }
}
