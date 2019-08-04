package com.zetcode;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class PredicateCompose {

    public static void main(String[] args) {

        int[] nums = {2, 3, 1, 5, 6, 7, 8, 9, 12};

        IntPredicate p1 = n -> n > 3;
        IntPredicate p2 = n -> n < 9;

        Arrays.stream(nums).filter(p1.and(p2)).forEach(System.out::println);
    }
}
