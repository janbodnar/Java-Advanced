package com.zetcode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapEx {

    public static void main(String[] args) {

        var vals1 = List.of(1, 2, 3);
        var vals2 = List.of(4, 5, 6);
        var vals3 = List.of(7, 8, 9);

        var vals = Stream.of(vals1, vals2, vals3)
                .collect(Collectors.toList());

        System.out.println("Before flattening : " + vals);

        var flattened = Stream.of(vals1, vals2, vals3).flatMap(Collection::stream)
                .collect(Collectors.toList());

        System.out.println("After flattening : " + flattened);
   }
}
