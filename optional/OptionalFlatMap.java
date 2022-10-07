package com.zetcode;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

// if the result is already an Optional flatMap
// does not wrap it within an additional Optional

public class OptionalFlatMap {

    public static void main(String[] args) {

        Function<String, Optional<String>> upperCase = s -> Optional.of(s.toUpperCase());

        var words = Arrays.asList("rock", null, "mountain",
                null, "falcon", "sky");

        for (int i = 0; i < 5; i++) {

            Optional<String> word = Optional.ofNullable(words.get(i));

            var res = word.flatMap(upperCase);
            res.ifPresent(System.out::println);
        }
    }
}
