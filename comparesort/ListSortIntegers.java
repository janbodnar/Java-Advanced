package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// sorting integers in-place

public class ListSortIntegers {

    public static void main(String[] args) {

        List<Integer> vals = Arrays.asList(5, -4, 0, 2, -1, 4, 7, 6, 1, -1, 3, 8, -2);
        vals.sort(Comparator.naturalOrder());
        System.out.println(vals);

        vals.sort(Comparator.reverseOrder());
        System.out.println(vals);
    }
}
