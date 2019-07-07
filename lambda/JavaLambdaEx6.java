package com.zetcode;

import java.util.List;
import java.util.function.Consumer;

public class JavaLambdaEx6 {

    public static void main(String[] args) {

        var names = List.of("Peter", "Johanna", "Roger", "Julia");

        names.forEach(((Consumer<String>) e -> System.out.println(e.toUpperCase()))
                .andThen(e -> System.out.println(e.toLowerCase())));
    }
}
