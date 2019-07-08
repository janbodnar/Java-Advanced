package com.zetcode;

import java.util.stream.IntStream;

public class JavaStreamReduceOptional2 {

    public static void main(String[] args) {

        IntStream.range(1, 10).reduce((x, y) -> x + y).ifPresent(s -> System.out.println(s));
        IntStream.range(1, 10).reduce(Integer::sum).ifPresent(s -> System.out.println(s));
        IntStream.range(1, 10).reduce(MyUtils::add2Ints).ifPresent(s -> System.out.println(s));
    }
}

