package com.zetcode;

import java.util.Map;

import static java.util.Map.entry;

// factory initialization methods since Java 9
public class HashMapInit {

    public static void main(String[] args) {

        // up to 10 elements:
        Map<Integer, String> colours = Map.of(
                1, "red",
                2, "blue",
                3, "brown"
        );

        System.out.println(colours);

        // any number of elements
        Map<String, String> countries = Map.ofEntries(
                entry("de", "Germany"),
                entry("sk", "Slovakia"),
                entry("ru", "Russia")
        );

        System.out.println(countries);
    }
}
