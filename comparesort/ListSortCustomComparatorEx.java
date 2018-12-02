package com.zetcode;

import java.util.Arrays;

// Comparing a list of words by their size with a custom comparator

public class ListSortCustomComparatorEx {

    public static void main(String[] args) {

        var words = Arrays.asList("pen", "blue", "atom", "to",
                "ecclesiastical", "abbey", "car", "ten", "desk", "slim",
                "journey", "forest", "landscape", "achievement", "Antarctica");

        words.sort((e1, e2) -> e1.length() - e2.length());

        words.forEach(System.out::println);

        words.sort((e1, e2) -> e2.length() - e1.length());

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
