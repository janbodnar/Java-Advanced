package com.zetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Compare lists by ignoring duplicates

public class CompareListsWithHashSetEx {

    public static void main(String[] args) {

        var words = new ArrayList<String>();
        var words2 = new ArrayList<String>();

        words.add("blue");
        words.add("green");
        words.add("red");
        words.add("yellow");

        words2.add("green");
        words2.add("blue");
        words2.add("red");
        words2.add("yellow");


        boolean equal = listEqualsIgnoreOrder(words, words2);

        if (equal) {
            System.out.println("The lists are equal");
        } else {
            System.out.println("The lists are not equal");
        }
    }

    private static <T> boolean listEqualsIgnoreOrder(List<T> l1, List<T> l2) {

        if (l1 == null || l2 == null) {
            return l1 == l2;
        }

        return new HashSet<>(l1).equals(new HashSet<>(l2));
    }
}
