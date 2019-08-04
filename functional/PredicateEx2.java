package com.zetcode;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateEx2 {

    public static void main(String args[]) {

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7,
                8, 9, 10, 11, 12);

        List<Integer> all = eval(list, n -> true);
        System.out.println(all);

        List<Integer> evenValues = eval(list, n -> n % 2 == 0);
        System.out.println(evenValues);

        List<Integer> greaterThanSix = eval(list, n -> n > 6);
        System.out.println(greaterThanSix);
    }

    private static List<Integer> eval(List<Integer> values,
                                     Predicate<Integer> predicate) {
        return values.stream().filter(predicate)
                .collect(Collectors.toList());
    }
}
