package com.zetcode;

import java.util.List;

// Java 8 introduced the forEach() method

public class ListForEachEx {

    public static void main(String[] args) {

        var items = List.of("coin", "ball", "lamp", "spoon");

        items.forEach(System.out::println);
    }
}
