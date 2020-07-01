package com.zetcode;

import java.util.List;
import java.util.function.Consumer;

public class ConsumerEx6 {

    public static void main(String[] args) {

        var data = List.of(1, 2, 3, 4, 5, 6, 7);

//      Consumer<Integer> consumer = (Integer x) ->  System.out.println(x);
        Consumer<Integer> consumer = System.out::println;
        forEach(data, consumer);

        System.out.println("--------------------------");

        forEach(data, System.out::println);
    }

    static <T> void forEach(List<T> data, Consumer<T> consumer) {

        for (T t : data) {
            consumer.accept(t);
        }
    }
}
