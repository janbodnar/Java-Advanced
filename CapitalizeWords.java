package com.zetcode;

import java.util.ArrayList;
import java.util.List;

// latin characters only

public class CapitalizeWords {

    public static void main(String[] args) {

        List<String> words = List.of("rock", "forest", "sky", "cloud", "water");
        List<String> capitalized = new ArrayList<>();

        for (var word : words) {

            var sb = new StringBuilder(word);

            char c = sb.charAt(0);
            char upperCased = Character.toUpperCase(c);
            sb.setCharAt(0, upperCased);

            capitalized.add(sb.toString());
        }

        System.out.println(capitalized);
    }
}
