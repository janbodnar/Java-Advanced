package com.zetcode;

import java.util.Arrays;
import java.util.Collections;

// Sorting arrays of Strings; a String is an Object

public class ArraysSortStringsEx {

    public static void main(String[] args) {

        String[] words = {"pen", "blue", "atom", "abbey", "car",
            "ten", "desk", "slim"};

        Arrays.sort(words);

        System.out.println(Arrays.toString(words));

        Arrays.sort(words, Collections.reverseOrder());

        System.out.println(Arrays.toString(words));
    }
}
