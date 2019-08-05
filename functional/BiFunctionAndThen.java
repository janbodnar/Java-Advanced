package com.zetcode;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BiFunctionAndThen {

    public static void main(String[] args) {

        BiFunction<Integer, Integer, Integer> sum = Integer::sum;
        Function<Integer, Integer> square = (n) -> n * n;

        System.out.println(sum.andThen(square).apply(5, 3));
        System.out.println(sum.andThen(square).apply(12, 2));
    }
}
