package com.zetcode;

import java.util.function.BiPredicate;

public class BiPredicateEx {

    public static void main(String[] args) {

        BiPredicate<Long, Long> pred1 = (x, y) -> x > y;
        BiPredicate<Long, Long> pred2 = Long::equals;

        // using and()
        System.out.println(pred1.and(pred2).test(7L, 7L));

        // using or()
        System.out.println(pred1.or(pred2).test(3L, 2L));

        // using negate()
        System.out.println(pred1.negate().test(4L, 1L));
    }
}
