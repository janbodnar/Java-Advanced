package com.zetcode;

import java.util.List;

// Java 8 introduced the forEach() method

public class ForEachEx {

    public static void main(String[] args) {

        var items = List.of("coin", "ball", "lamp", "spoon");

        items.stream().forEach(System.out::println);
    }
}
