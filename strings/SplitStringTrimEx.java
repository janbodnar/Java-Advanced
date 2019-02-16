package com.zetcode;

import java.util.Arrays;

public class SplitStringTrimEx {

    public static void main(String[] args) {

        var input = " wood, falcon\t, sky, forest\n";

        var output = input.trim().split("\\s*,\\s*");

        Arrays.stream(output).forEach(System.out::println);
    }
}
