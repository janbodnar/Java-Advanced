package com.zetcode;

import java.util.Optional;

public class OptionalOrElse {

    public static void main(String[] args) {

        Optional<String> word1 = Optional.of("sky");
        Optional<String> word2 = Optional.empty();

        System.out.println(word1.orElse("n/a"));
        System.out.println(word2.orElse("n/a"));
    }
}
