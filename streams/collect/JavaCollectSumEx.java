package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;

public class JavaCollectSumEx {

    public static void main(String[] args) {

        var vals = List.of(2, 4, 6, 8, 10, 12);

        // can be replaced with mapToInt().sum()
        var sum = vals.stream().collect(Collectors.summingInt(Integer::intValue));

        System.out.printf("The sum of values is %d%n", sum);
    }
}
