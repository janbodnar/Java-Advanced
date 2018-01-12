package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaStreamFilterStringLength {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("pen", "custom", "orphanage",
                "forest", "bubble", "butterfly");

        List<String> result = words.stream().filter(word -> word.length() > 5)
                .collect(Collectors.toList());

        result.forEach(word -> System.out.println(word));
    }
}
