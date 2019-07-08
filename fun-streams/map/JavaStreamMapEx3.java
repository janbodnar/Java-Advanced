package com.zetcode;

import java.util.stream.Stream;

public class JavaStreamMapEx3 {

    public static void main(String[] args) {

        var words = Stream.of("cardinal", "pen", "coin", "globe");
        words.map(JavaStreamMapEx3::capitalize).forEach(System.out::println);
    }

    private static String capitalize(String word) {

        word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        return word;
    }
}
