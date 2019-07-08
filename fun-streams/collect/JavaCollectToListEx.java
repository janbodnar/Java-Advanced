package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;

public class JavaCollectToListEx {

    public static void main(String[] args) {

        var words = List.of("marble", "coin", "forest", "falcon",
                "sky", "cloud", "eagle", "lion");

        // filter all four character words into a list
        var words4 = words.stream().filter(word -> word.length() == 4)
                .collect(Collectors.toList());

        System.out.println(words4);
    }
}
