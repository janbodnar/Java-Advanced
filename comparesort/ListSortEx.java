package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Sorting a list of strings

public class ListSortEx {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("dog", "Sun", "Earth", "pen", "plum", "forest", 
                "atom", "water", "sky", "meadow", "lowland", "swim", "eat", "drum");
        
        words.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(words);
        
        words.sort(Comparator.naturalOrder());
        System.out.println(words);
        
        words.sort(Comparator.reverseOrder());
        System.out.println(words);
    }
}
