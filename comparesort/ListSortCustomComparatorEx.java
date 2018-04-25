package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Comparing a list of words by their size with a custom comparator

public class ListSortCustomComparatorEx {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("pen", "blue", "atom", "to",
                "ecclesiastical", "abbey", "car", "ten", "desk", "slim",
                "journey", "forest", "landscape", "achievement", "Antarctica");

        words.sort((e1, e2) -> {
            return e1.length() - e2.length();
        });

        words.forEach(System.out::println);

        words.sort((e1, e2) -> {
            return e2.length() - e1.length();
        });
        
//        words.sort(new Comparator<String>() {
//            @Override
//            public int compare(String e1, String e2) {
//                return e2.length() - e1.length();
//
//            }
//        });
        
        words.forEach(System.out::println);
    }
}
