package com.zetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//https://stackoverflow.com/questions/1075656/simple-way-to-find-if-two-different-lists-contain-exactly-the-same-elements?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

public class CompareListsEx {

    public static void main(String[] args) {

        // Solution one
        List<String> words = Arrays.asList("dog", "pen", "sky", "rock");
        List<String> words2 = Arrays.asList("dog", "pen", "sky", "rock");

        // O(n^2)
        boolean equal = (words.size() == words2.size()) && words.containsAll(words2);

        if (equal) {

            System.out.println("Lists are equal");
        } else {
            System.out.println("Lists are not equal");
        }

        // Solution two
        List<String> listOne = new ArrayList(Arrays.asList("dog", "pen", "sky", "rock"));
        List<String> listTwo = new ArrayList(Arrays.asList("dog", "pen", "sky", "rock"));

        listOne.retainAll(listTwo);
        System.out.println(listOne);

        // Solution three
        List<String> words3 = new ArrayList<>();
        List<String> words4 = new ArrayList<>();

        words3.add("blue");
        words3.add("green");
        words3.add("red");
        words3.add("yellow");

        words4.add("blue");
        words4.add("green");
        words4.add("red");
        words4.add("yellow");

        if (Objects.equals(words3, words4)) {

            System.out.println("Lists are equal");
        } else {

            System.out.println("Lists are not equal");
        }
    }
}
