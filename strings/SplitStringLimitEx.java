package com.zetcode;

import java.util.Arrays;

public class SplitStringLimitEx {

    public static void main(String[] args) {

        var names = "Jane-Paul-Ferenc-David-Robert-Julia";

        var output = names.split("-", 4);

        Arrays.stream(output).forEach(System.out::println);
    }
}
