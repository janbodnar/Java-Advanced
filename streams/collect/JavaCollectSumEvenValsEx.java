package com.zetcode;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaCollectSumEvenValsEx {

    public static void main(String[] args) {

        var vals = IntStream.of(1, 2, 3, 4, 5, 6, 7, 8);

        // can be replaced with mapToInt().sum()
        var sumEven = vals.boxed().collect(Collectors.summingInt((e -> e % 2 == 0 ? e : 0)));
        System.out.printf("The sum of even values is: %d%n", sumEven);
    }
}
