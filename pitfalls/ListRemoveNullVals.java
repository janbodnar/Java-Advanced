package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Arrays.asList() returns return new ArrayList<>(a); where ArrayList is
// not java.util.ArrayList, but java.util.Arrays.ArrayList (internal class),
// which does not allow removal.

public class ListRemoveNullVals {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("forest", null, "falcon",
                "eagle", "rock", null, "sky", "cloud");

        words.forEach(System.out::println);

        // Exception in thread "main" java.lang.UnsupportedOperationException: remove
        words.removeIf(Objects::isNull);

        words.forEach(System.out::println);
    }
}
