package com.zetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Compare lists by sorting and transferring to strings
// We assume that in-place sorting is OK

public class CompareListsEx2 {

    public static boolean compareList(List<String> l1, List<String> l2) {

        return l1.toString().contentEquals(l2.toString());
    }

    public static void main(String[] args) {

        var one = new ArrayList<String>();
        var two = new ArrayList<String>();

        one.add("dog");
        one.add("pen");
        one.add("sky");
        one.add("rock");

        two.add("dog");
        two.add("pen");
        two.add("rock");
        two.add("sky");

        one.sort(Comparator.naturalOrder());
        two.sort(Comparator.naturalOrder());

        System.out.println(one);
        System.out.println(two);

        if (compareList(one, two)) {

            System.out.println("The lists are equal");
        } else {

            System.out.println("The lists are not equal");
        }
    }
}
