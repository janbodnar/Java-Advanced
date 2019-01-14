package com.zetcode;

import com.zetcode.utils.MathUtils;

import java.util.List;

public class Application {

    public static void main(String[] args) {

        var vals = List.of(2, 4, -2, 4, 0, -1, 9, 2, -3);

        System.out.println("Positive values");

        var poss = MathUtils.positive(vals);
        poss.forEach(System.out::println);

        System.out.println("Negative values");
        var negs = MathUtils.negative(vals);
        negs.forEach(System.out::println);

        System.out.println("The sum of values");
        var sum = MathUtils.sum(vals);
        System.out.println(sum);
    }
}
