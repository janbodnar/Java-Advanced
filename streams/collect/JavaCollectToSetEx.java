package com.zetcode;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaCollectToSetEx {

    public static void main(String[] args) {

        var vals = Stream.of("a", "b", "c", "a",
                "b", "c", "a", "a");

        Set<String> uniqueVals = vals
                .collect(Collectors.toSet());

        System.out.println(uniqueVals);
    }
}
