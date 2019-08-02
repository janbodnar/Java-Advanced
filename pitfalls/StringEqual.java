package com.zetcode;

import java.util.Arrays;

// the program throws a NullPointerException
// we should swap the sides: "rock".equals(word)

public class StringEqual {

    public static void main(String[] args) {

        var words = Arrays.asList("wood", "forest", "falcon", null, "sky", "rock");

        for (var word : words) {

            if (word.equals("rock")) {

                System.out.println("The list contains rock");
            }
        }
    }
}
