package com.zetcode;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

// IntPredicate represents a predicate (boolean-valued function) of one int-valued
// argument. This is the int-consuming primitive type specialization of
// Predicate.

public class IntPredicateEx {

    public static void main(String[] args) {

        IntStream intStream = IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        IntStream filtered = intStream.filter(new IntPredicate() {

            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        });

        filtered.forEach(System.out::println);
    }
}
