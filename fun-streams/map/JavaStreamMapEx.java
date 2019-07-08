package com.zetcode;

import java.util.Arrays;
import java.util.stream.IntStream;

public class JavaStreamMapEx {

    public static void main(String[] args) {

        var nums = IntStream.of(1, 2, 3, 4, 5, 6, 7, 8);

        var squares = nums.map(e -> e * e).toArray();

        System.out.println(Arrays.toString(squares));
    }
}
