package com.zetcode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaCollectMinMaxEx {

    public static void main(String[] args) {

        var vals = List.of(1, 2, 3, 4, 5, 6, 7);

        // can be replaced with min()
        Optional<Integer> min = vals.stream().collect(Collectors.minBy(Integer::compareTo));

        // can be replaced with max()
        Optional<Integer> max = vals.stream().collect(Collectors.maxBy(Integer::compareTo));

        min.ifPresent(val -> System.out.printf("Minimum is %d%n", val));
        max.ifPresent(val -> System.out.printf("Maximum is %d%n", val));
    }
}
