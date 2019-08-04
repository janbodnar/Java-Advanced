package com.zetcode;

import java.util.function.BiConsumer;

public class BiConsumerAndThen {

    public static void main(String[] args) {

        BiConsumer<Integer, Integer> add = (a, b) -> System.out.println(a + b);
        BiConsumer<Integer, Integer> sub = (a, b) -> System.out.println(a - b);

        add.andThen(sub).accept(10, 6);
    }
}
