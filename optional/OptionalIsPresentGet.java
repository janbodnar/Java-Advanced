package com.zetcode;

import java.util.Arrays;
import java.util.Optional;

public class OptionalIsPresentGet {

    public static void main(String[] args) {

        var words = Arrays.asList("rock", null, "mountain",
                null, "falcon", "sky");

        for (String s : words) {

            Optional<String> word = Optional.ofNullable(s);

            if (word.isPresent()) {

                System.out.println(word.get());
            } else {

                System.out.println("Invalid value");
            }
        }
    }
}
