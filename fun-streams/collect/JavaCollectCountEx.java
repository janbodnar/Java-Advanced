package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;

public class JavaCollectCountEx {

    public static void main(String[] args) {

        var vals = List.of(1, 2, 3, 4, 5);

        // can be replaced with count
        var n = vals.stream().collect(Collectors.counting());

        System.out.println(n);
    }
}
