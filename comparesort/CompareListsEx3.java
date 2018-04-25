package com.zetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Compare lists by copying lists, sorting, and transforming to strings

public class CompareListsEx3 {
    
    public static boolean compareList(List<String> ls1, List<String> ls2) {

        return ls1.toString().contentEquals(ls2.toString());
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
        
        List<String> copyWords = new ArrayList<>();
        copyWords.addAll(words);
        
        List<String> copyWords2 = new ArrayList<>();
        copyWords2.addAll(words2);      
        
        copyWords.sort(Comparator.naturalOrder());
        copyWords2.sort(Comparator.naturalOrder());
        
        boolean equal = compareList(copyWords, copyWords2);
        
        if (equal) {

            System.out.println("Lists are equal");
        } else {

            System.out.println("Lists are not equal");
        }        
    }
}
