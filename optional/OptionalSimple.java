package com.zetcode;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class OptionalSimple {

    public static void main(String[] args) {

        var words = Arrays.asList("rock", null, "mountain",
                null, "falcon", "sky");

        for (int i = 0; i < 5; i++) {

            Optional<String> word = Optional.ofNullable(words.get(
                    new Random().nextInt(words.size())));
            word.ifPresent(System.out::println);
        }
    }
}
