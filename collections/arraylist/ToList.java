package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToList {

    public static void main(String[] args) {

        var words = Stream.of("forest", "eagle", "river", "cloud", "sky");

        List<String> words2 = words.collect(Collectors.toList());
        System.out.println(words2.getClass());
    }
}
