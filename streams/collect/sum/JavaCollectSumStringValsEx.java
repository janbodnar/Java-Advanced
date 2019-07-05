package com.zetcode;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaCollectSumStringValsEx {

    public static void main(String[] args) {

        Stream<String> vals = Stream.of("1", "2", "3", "5", "6");

        // can be replaced with mapToInt().sum()
        var sum = vals.collect(Collectors.summingInt(n -> Integer.parseInt(n)));

        System.out.println(sum);
    }
}
