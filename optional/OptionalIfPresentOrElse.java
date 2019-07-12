package com.zetcode;

import java.util.Arrays;
import java.util.Optional;

public class OptionalIfPresentOrElse {

    public static void main(String[] args) {

        var words = Arrays.asList("rock", null, "mountain",
                null, "falcon", "sky");

        for (String val : words) {

            Optional<String> word = Optional.ofNullable(val);

            word.ifPresentOrElse(System.out::println,
                    () -> System.out.println("Invalid value"));
        }
    }
}
