package com.zetcode;

import java.util.Random;
import java.util.stream.Stream;

public class JavaStreamMapEx2 {

    public static void main(String[] args) {

        Stream.generate(new Random()::nextDouble)
                .map(e -> (e * 100))
                .mapToInt(Double::intValue)
                .limit(5)
                .forEach(System.out::println);
    }
}
