package com.zetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Compare lists by sorting and transfering to strings
// We assume that in-place sorting is OK

public class CompareListsEx2 {

    public static boolean compareList(List<String> l1, List<String> l2) {

        return l1.toString().contentEquals(l2.toString());
    }

    public static void main(String[] args) {

        List<String> words = new ArrayList<>();
        List<String> words2 = new ArrayList<>();

        words.add("dog");
        words.add("pen");
        words.add("sky");
        words.add("rock");

        words2.add("dog");
        words2.add("pen");
        words2.add("rock");
        words2.add("sky");

        words.sort(Comparator.naturalOrder());
        words2.sort(Comparator.naturalOrder());

        System.out.println(words);
        System.out.println(words2);

        boolean equal = compareList(words, words2);

        if (equal) {

            System.out.println("Lists are equal");
        } else {

            System.out.println("Lists are not equal");
        }

    }
}

