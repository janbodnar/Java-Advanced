package com.zetcode;

import java.util.Arrays;
import java.util.List;

// Java 8 introduced the forEach() method

public class JavaForEachEx {

    public static void main(String[] args) {

        List<String> items = Arrays.asList("coin", "ball", "lamp", "spoon");
        
        items.stream().forEach(System.out::println);
    }
}
