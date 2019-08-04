package com.zetcode;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ConsumerEx2 {

    public static void main(String[] args) {

        Consumer<Integer> printMultiplyBy100 = (val) -> System.out.println(val * 100);

        printMultiplyBy100.accept(3);
        printMultiplyBy100.accept(4);
        printMultiplyBy100.accept(5);

        IntConsumer printMultiplyBy500 = a -> System.out.println(a * 50);
        printMultiplyBy500.accept(1);
        printMultiplyBy500.accept(2);
        printMultiplyBy500.accept(3);
    }
}
