package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodReferenceEx2 {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>();
        words.add("forest");
        words.add(null);
        words.add("falcon");
        words.add("eagle");
        words.add("rock");
        words.add(null);
        words.add("sky");
        words.add("cloud");

        words.forEach(System.out::println);

        words.removeIf(Objects::isNull);

        System.out.println("Null values removed:");
        words.forEach(System.out::println);
    }
}
