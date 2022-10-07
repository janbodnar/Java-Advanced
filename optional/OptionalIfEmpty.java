package com.zetcode;

import java.util.Arrays;
import java.util.Optional;

public class OptionalIfEmpty {

    public static void main(String[] args) {

        var words = Arrays.asList("rock", null, "mountain",
                null, "falcon", "sky");

        for (int i = 0; i < 5; i++) {

            Optional<String> word = Optional.ofNullable(words.get(i));

            if (word.isEmpty()) {

                System.out.println("n/a");
            }

            word.ifPresent(System.out::println);
        }
    }
}
