package com.zetcode;

import java.util.List;
import java.util.function.Function;

// Represents a function that accepts one argument and produces a result.
// <T> the type of the input to the function
// <R> the type of the result of the function

public class FunctionEx {

    public static void main(String[] args) {

        var vals = List.of(1, 2, 3, 4, 5);

        Function<List<Integer>, Integer> sumList = FunctionEx::doSumList;

        var mysum = sumList.apply(vals);
        System.out.println(mysum);
    }

    private static Integer doSumList(List<Integer> vals) {

        return vals.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
