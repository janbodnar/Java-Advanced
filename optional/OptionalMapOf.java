package com.zetcode;

import java.util.Optional;
import java.util.function.Function;

// if the result is already an Optional flatMap
// does not wrap it within an additional Optional

public class OptionalMapOf {

    public static void main(String[] args) {

        Function<String, Optional<String>> upperCase
                = s -> (s == null) ? Optional.empty() : Optional.of(s.toUpperCase());

        Optional<String> word = Optional.of("falcon");

        Optional<Optional<String>> opt1 = word.map(upperCase);
        Optional<String> opt2 = word.flatMap(upperCase);

        opt1.ifPresent(s -> s.ifPresent(System.out::println));
        opt2.ifPresentOrElse(System.out::println,
                () -> System.out.println("Invalid value"));
    }
}
